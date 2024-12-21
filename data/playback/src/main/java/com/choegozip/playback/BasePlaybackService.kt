package com.choegozip.playback

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.pm.PackageManager
import android.os.Build
import androidx.annotation.OptIn
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.os.bundleOf
import androidx.media3.common.AudioAttributes
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.util.EventLogger
import androidx.media3.session.MediaConstants
import androidx.media3.session.MediaLibraryService
import androidx.media3.session.MediaSession.ControllerInfo

open class BasePlaybackService : MediaLibraryService() {

    private lateinit var mediaLibrarySession: MediaLibrarySession

    companion object {
        private const val NOTIFICATION_ID = 123
        private const val CHANNEL_ID = "demo_session_notification_channel_id"
    }

    open fun getSingleTopActivity(): PendingIntent? = null

    @OptIn(UnstableApi::class)
    override fun onCreate() {
        super.onCreate()
        initializeSessionAndPlayer()
        setListener(MediaSessionServiceListener())
    }

    override fun onGetSession(controllerInfo: ControllerInfo): MediaLibrarySession {
        return mediaLibrarySession
    }

    @OptIn(UnstableApi::class)
    override fun onDestroy() {
        mediaLibrarySession.release()
        mediaLibrarySession.player.release()
        clearListener()
        super.onDestroy()
    }

    /**
     * 미디어 세션과 플레이어 초기화
     */
    private fun initializeSessionAndPlayer() {
        val player = ExoPlayer.Builder(this)
            .setAudioAttributes(AudioAttributes.DEFAULT, /* handleAudioFocus= */ true)
            .build().apply {

            }
        player.addAnalyticsListener(EventLogger())

        mediaLibrarySession =
            MediaLibrarySession.Builder(
                this,
                player,
                object : MediaLibrarySession.Callback {}
            )
            .also { builder -> getSingleTopActivity()?.let { builder.setSessionActivity(it) } }
            .build()
            .also { mediaLibrarySession ->
                // The media session always supports skip, except at the start and end of the playlist.
                // Reserve the space for the skip action in these cases to avoid custom actions jumping
                // around when the user skips.
                mediaLibrarySession.setSessionExtras(
                    bundleOf(
                        MediaConstants.EXTRAS_KEY_SLOT_RESERVATION_SEEK_TO_PREV to true,
                        MediaConstants.EXTRAS_KEY_SLOT_RESERVATION_SEEK_TO_NEXT to true,
                    )
                )
            }
    }

    @OptIn(UnstableApi::class)
    private inner class MediaSessionServiceListener : Listener {
        override fun onForegroundServiceStartNotAllowedException() {
            if (
                Build.VERSION.SDK_INT >= 33 &&
                checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) !=
                PackageManager.PERMISSION_GRANTED
            ) {
                // Notification permission is required but not granted
                return
            }

            val notificationManagerCompat = NotificationManagerCompat.from(this@BasePlaybackService)
            ensureNotificationChannel(notificationManagerCompat)

            // TODO 현재 클래스 문자열들 리소스화
            val notificationMessage = """
                미디어 알림에 재생 버튼이 아직 있으면 눌러주세요. 그렇지 않으면 앱을 열어 재생을 시작하고 세션을 컨트롤러에 다시 연결해 주세요.
            """.trimIndent()

            val builder =
                NotificationCompat.Builder(this@BasePlaybackService, CHANNEL_ID)
                    .setSmallIcon(android.R.drawable.btn_star)
                    .setContentTitle("재생을 재개할 수 없습니다.")
                    .setStyle(
                        NotificationCompat.BigTextStyle().bigText(notificationMessage)
                    )
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setAutoCancel(true)
            notificationManagerCompat.notify(NOTIFICATION_ID, builder.build())
        }
    }

    /**
     * 안내 채널 생성
     */
    private fun ensureNotificationChannel(notificationManagerCompat: NotificationManagerCompat) {
        if (notificationManagerCompat.getNotificationChannel(CHANNEL_ID) != null) {
            return
        }

        val channel =
            NotificationChannel(
                CHANNEL_ID,
                "재생을 재개할 수 없습니다",
                NotificationManager.IMPORTANCE_DEFAULT,
            )
        notificationManagerCompat.createNotificationChannel(channel)
    }
}
