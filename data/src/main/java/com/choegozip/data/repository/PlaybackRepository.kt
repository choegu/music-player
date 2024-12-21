package com.choegozip.data.repository

import android.content.ComponentName
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.choegozip.data.service.PlaybackService
import com.choegozip.domain.model.Media
import com.choegozip.domain.model.ComponentInfo
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

    private lateinit var uiComponentInfo: ComponentInfo
    private lateinit var controller: MediaController

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

        controller.duration
        controller.currentPosition

        return playbackComponentInfo
    }

    suspend fun getPlaybackState(): PlaybackState {
        return withContext(Dispatchers.Main) {
            PlaybackState(
                duration = controller.duration,
                currentPosition = controller.currentPosition
            )
        }
    }

    suspend fun playMedia(mediaList: List<Media>) {
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
                setMediaItems(mediaItemList)
                shuffleModeEnabled = false
                prepare()
                play()
            }
        }
    }

    fun restoreController() {

    }

}