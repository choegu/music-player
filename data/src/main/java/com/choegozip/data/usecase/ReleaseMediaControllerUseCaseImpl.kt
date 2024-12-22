package com.choegozip.data.usecase

import com.choegozip.data.repository.PlaybackRepository
import com.choegozip.domain.usecase.ReleaseMediaControllerUseCase
import javax.inject.Inject

class ReleaseMediaControllerUseCaseImpl @Inject constructor(
    private val playbackRepository: PlaybackRepository
): ReleaseMediaControllerUseCase {

    override suspend fun invoke(): Result<Unit> = runCatching {
        playbackRepository.removeListeners()
        playbackRepository.releaseController()
    }
}