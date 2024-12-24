package com.choegozip.data.repository

import android.content.ComponentName
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.annotation.VisibleForTesting
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.choegozip.data.model.toDomainModel
import com.choegozip.data.preference.MediaControllerSharedPrefs
import com.choegozip.data.service.PlaybackService
import com.choegozip.domain.model.media.Media
import com.choegozip.domain.model.ComponentInfo
import com.choegozip.domain.model.playback.PlayMedia
import com.choegozip.domain.model.playback.PlaybackPosition
import com.google.common.util.concurrent.ListenableFuture
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.guava.await
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PlaybackRepository @Inject constructor(
    private val context: Context,
    private val mediaControllerSharedPrefs: MediaControllerSharedPrefs,
) {

    // TODO 클래스 초기화 된 경우 데이터스토어에 있는 문자열로 새로 생성
    private lateinit var controllerFuture: ListenableFuture<MediaController>
    private lateinit var controller: MediaController

    // 재생 리스너들
    private lateinit var playWhenReadyListener: Player.Listener
    private lateinit var positionChangedListener: Player.Listener
    private lateinit var mediaItemTransitionListener: Player.Listener

    /**
     * 프레젠테이션 모듈 UI 정보
     */
    fun getUiComponentInfo() = mediaControllerSharedPrefs.getUiComponent()

    /**
     * UI 컴포넌트 전달하면서, 재생 컴포넌트 정보 가져오기
     *
     * data 모듈에서 세션액티비티로 사용하기 위해, presentation 모듈의 액티비티 정보가 필요하다.
     * presentation 모듈에서 playerView 에 연결하기 위해, data 모듈의 mediaController 정보가 필요하다.
     * 각 ComponentName 정보를 domain 모듈을 통해 String으로 전달하여, ComponentName 을 구성한다.
     */
    suspend fun getPlaybackComponent(uiComponentInfo: ComponentInfo): ComponentInfo {
        // 기존 리스너 제거
        removeListeners()

        mediaControllerSharedPrefs.setUiComponent(uiComponentInfo)

        val playbackServiceComponent = ComponentName(context, PlaybackService::class.java)
        val playbackComponentInfo = ComponentInfo(
            packageName = playbackServiceComponent.packageName,
            className = playbackServiceComponent.className
        )

        val sessionToken = SessionToken(context, playbackServiceComponent)

        controllerFuture =
            MediaController.Builder(
                context,
                sessionToken,
            ).buildAsync()
        controller = controllerFuture.await()

        return playbackComponentInfo
    }

    /**
     * 재생 상태 구독하기
     */
    suspend fun getPlayWhenReady(flow: MutableSharedFlow<Boolean>) {
        withContext(Dispatchers.Main) {
            playWhenReadyListener = object : Player.Listener {
                override fun onPlayWhenReadyChanged(playWhenReady: Boolean, reason: Int) {
                    Log.d("PlayerState", "PlayWhenReady changed: $playWhenReady, Reason: $reason")
                    flow.tryEmit(playWhenReady)
                }
            }
            controller.addListener(playWhenReadyListener)

            // 연결 직후 1회 방출
            flow.tryEmit(controller.playWhenReady)
        }
    }

    /**
     * 재생 포지션 변경 상태 구독하기
     */
    suspend fun getPositionChanged(flow: MutableSharedFlow<PlaybackPosition>) {
        withContext(Dispatchers.Main) {
            positionChangedListener = object : Player.Listener {
                override fun onPositionDiscontinuity(
                    oldPosition: Player.PositionInfo,
                    newPosition: Player.PositionInfo,
                    reason: Int
                ) {
                    super.onPositionDiscontinuity(oldPosition, newPosition, reason)
                    flow.tryEmit(
                        PlaybackPosition(
                            duration = controller.duration,
                            currentPosition = controller.currentPosition
                        )
                    )
                }
            }
            controller.addListener(positionChangedListener)

            // 연결 직후 1회 방출
            flow.tryEmit(
                PlaybackPosition(
                    duration = controller.duration,
                    currentPosition = controller.currentPosition
                )
            )
        }
    }

    /**
     * 미디어 변경 트랜지션 구독하기
     */
    suspend fun getMediaItemTransition(flow: MutableSharedFlow<Media>) {
        withContext(Dispatchers.Main) {
            mediaItemTransitionListener = object : Player.Listener {
                override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                    flow.tryEmit(mediaItem.toDomainModel())
                }
            }
            controller.addListener(mediaItemTransitionListener)

            // 연결 직후 1회 방출
            flow.tryEmit(controller.currentMediaItem.toDomainModel())
        }
    }

    /**
     * 재생 포지션 가져오기
     */
    suspend fun getPlaybackPosition(): PlaybackPosition {
        return withContext(Dispatchers.Main) {
            PlaybackPosition(
                duration = controller.duration,
                currentPosition = controller.currentPosition
            )
        }
    }

    /**
     * 특정 미디어 재생하기
     */
    suspend fun playMedia(playMedia: PlayMedia, mediaList: List<Media>) {
        // 미디어 아이템 변환
        val mediaItemList = mediaList.map {
            // 음악 파일의 URI
            val contentUri = Uri.withAppendedPath(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, it.id.toString())
            // 앨범 아트 URI
            val albumArtUri = Uri.parse("content://media/external/audio/albumart/${it.albumId}")
            // MediaItem 생성
            MediaItem.Builder()
                .setMediaId(it.id.toString())
                .setUri(contentUri)
                .setMediaMetadata(
                    MediaMetadata.Builder()
                        .setTitle(it.title)
                        .setArtist(it.artist)
                        .setAlbumTitle(it.albumTitle)
                        .setArtworkUri(albumArtUri)
                        .setDescription(it.displayName)
                        .setExtras(
                            Bundle().apply {
                                putLong("albumId", it.albumId)
                                putLong("duration", it.duration)
                            }
                        )
                        .build()
                )
                .build()
        }

        withContext(Dispatchers.Main) {
            controller.run {
                // 시작 인덱스 체크
                playMedia.mediaIndex?.let { mediaIndex ->
                    setMediaItems(
                        mediaItemList,
                        mediaIndex,
                        C.TIME_UNSET
                    )
                } ?: run {
                    setMediaItems(mediaItemList)
                }
                shuffleModeEnabled = playMedia.shuffleModeEnabled
                prepare()
                play()
            }
        }
    }

    /**
     * 현재 미디어 재생 또는 일시정지
     */
    suspend fun playOrPauseMedia() {
        withContext(Dispatchers.Main) {
            controller.run {
                if (playWhenReady) {
                    pause()
                } else {
                    play()
                }
            }
        }
    }

    /**
     * 리스너 제거
     */
    suspend fun removeListeners() {
        withContext(Dispatchers.Main) {
            // TODO 휴먼에러 발생 여지 크다... 컨트롤러 및 리스너 관리 클래스 추가하기
            if (::controller.isInitialized) {
                if (::playWhenReadyListener.isInitialized)
                    controller.removeListener(playWhenReadyListener)
                if (::positionChangedListener.isInitialized)
                    controller.removeListener(positionChangedListener)
                if (::mediaItemTransitionListener.isInitialized)
                    controller.removeListener(mediaItemTransitionListener)
            }
        }
    }

    /**
     * 컨트롤러 종료
     */
    fun releaseController() {
        MediaController.releaseFuture(controllerFuture)
    }

    /**
     * 컨트롤러 강제 세팅
     *
     * TODO 해당 함수 없이 테스트 가능한 구조 고민하기
     */
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun setController(controller: MediaController) {
        this.controller = controller
    }
}