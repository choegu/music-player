package com.choegozip.data.service

import android.app.PendingIntent
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.app.PendingIntent.getActivity
import android.content.ComponentName
import android.content.Intent
import com.choegozip.data.repository.PlaybackRepository
import com.choegozip.playback.BasePlaybackService
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PlaybackService : BasePlaybackService() {

    @Inject
    lateinit var playbackRepository: PlaybackRepository

    override fun getSingleTopActivity(): PendingIntent? {
        val uiComponentInfo = playbackRepository.getUiComponentInfo()

        val intent =
            Intent()
                .setComponent(
                    ComponentName(
                        uiComponentInfo.packageName,
                        uiComponentInfo.className
                    )
                ).apply {
                    flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
                }

        return getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE or FLAG_UPDATE_CURRENT
        )
    }
}
