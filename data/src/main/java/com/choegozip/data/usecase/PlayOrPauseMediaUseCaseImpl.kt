package com.choegozip.data.usecase

import com.choegozip.data.repository.PlaybackRepository
import com.choegozip.domain.usecase.PlayOrPauseMediaUseCase
import javax.inject.Inject

class PlayOrPauseMediaUseCaseImpl @Inject constructor(
    private val playbackRepository: PlaybackRepository
): PlayOrPauseMediaUseCase {

    override suspend fun invoke(): Result<Unit> = runCatching {
        playbackRepository.playOrPauseMedia()
    }
}