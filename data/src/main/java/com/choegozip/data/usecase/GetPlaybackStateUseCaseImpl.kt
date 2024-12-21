package com.choegozip.data.usecase

import com.choegozip.data.repository.PlaybackRepository
import com.choegozip.domain.model.PlaybackState
import com.choegozip.domain.usecase.GetPlaybackStateUseCase
import javax.inject.Inject

class GetPlaybackStateUseCaseImpl @Inject constructor(
    private val playbackRepository: PlaybackRepository
): GetPlaybackStateUseCase {

    override suspend fun invoke(): Result<PlaybackState> = runCatching {
        playbackRepository.getPlaybackState()
    }
}