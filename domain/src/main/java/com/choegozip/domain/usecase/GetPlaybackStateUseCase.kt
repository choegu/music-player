package com.choegozip.domain.usecase

import com.choegozip.domain.model.PlaybackState

interface GetPlaybackStateUseCase {
    suspend operator fun invoke(): Result<PlaybackState>
}