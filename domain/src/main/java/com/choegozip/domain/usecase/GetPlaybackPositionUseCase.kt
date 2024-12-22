package com.choegozip.domain.usecase

import com.choegozip.domain.model.PlaybackPosition

interface GetPlaybackPositionUseCase {
    suspend operator fun invoke(): Result<PlaybackPosition>
}