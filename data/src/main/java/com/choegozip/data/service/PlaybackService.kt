package com.choegozip.data.service

import android.app.PendingIntent
import com.choegozip.data.repository.PlaybackRepository
import com.choegozip.playback.BasePlaybackService
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PlaybackService : BasePlaybackService() {

    @Inject
    lateinit var playbackRepository: PlaybackRepository

    override fun getSingleTopActivity(): PendingIntent? {


//        val intent = Intent(this, MainActivity::class.java).apply {
//            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
//        }
//        return getActivity(
//            this,
//            0,
//            intent,
//            PendingIntent.FLAG_IMMUTABLE or FLAG_UPDATE_CURRENT
//        )

        return null
    }
}
