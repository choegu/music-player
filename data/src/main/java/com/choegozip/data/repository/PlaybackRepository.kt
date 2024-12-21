package com.choegozip.data.repository

import android.content.ComponentName
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.choegozip.data.service.PlaybackService
import com.choegozip.domain.model.Media
import com.choegozip.domain.model.ComponentInfo
import com.choegozip.domain.model.PlayMedia
import com.choegozip.domain.model.PlaybackState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.guava.await
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PlaybackRepository @Inject constructor(
    private val context: Context
) {

    // TODO 클래스 초기화 대비하여 데이터스토어 저장
    private lateinit var uiComponentInfo: ComponentInfo

    // TODO 클래스 초기화 된 경우 데이터스토어에 있는 문자열로 새로 생성
    private lateinit var controller: MediaController

    /**
     * 프레젠테이션 모듈 UI 정보
     */
    fun getUiComponentInfo() = uiComponentInfo

    /**
     * 재생 컴포넌트 정보 가져오기
     */
    suspend fun getPlaybackComponent(uiComponentInfo: ComponentInfo): ComponentInfo {

        this.uiComponentInfo = uiComponentInfo

        val playbackServiceComponent = ComponentName(context, PlaybackService::class.java)
        val playbackComponentInfo = ComponentInfo(
            packageName = playbackServiceComponent.packageName,
            className = playbackServiceComponent.className
        )

        val sessionToken = SessionToken(context, playbackServiceComponent)

        controller =
            MediaController.Builder(
                context,
                sessionToken,
            ).buildAsync().await()

        controller.addListener(
            object : Player.Listener {
                override fun onEvents(player: Player, events: Player.Events) {
                    Log.d("!!!!!", "events : $events")
                }
            }
        )

        return playbackComponentInfo
    }

    /**
     * 재생 상태 가져오기
     */
    suspend fun getPlaybackState(): PlaybackState {
        return withContext(Dispatchers.Main) {
            PlaybackState(
                duration = controller.duration,
                currentPosition = controller.currentPosition
            )
        }
    }

    /**
     * 미디어 재생하기
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
                                putString("sourceUri", it.data)
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
     * 컨트롤러 종료
     * TODO
     */
    fun releaseController() {

    }

}