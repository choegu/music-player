package com.choegozip.domain.usecase.playback

interface ReleaseMediaControllerUseCase {
    suspend operator fun invoke(): Result<Unit>
}