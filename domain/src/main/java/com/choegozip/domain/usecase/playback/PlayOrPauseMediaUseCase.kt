package com.choegozip.domain.usecase.playback

interface PlayOrPauseMediaUseCase {
    suspend operator fun invoke(): Result<Unit>
}