package com.choegozip.domain.usecase.playback

import com.choegozip.domain.model.playback.PlaybackPosition

interface GetPlaybackPositionUseCase {
    suspend operator fun invoke(): Result<PlaybackPosition>
}