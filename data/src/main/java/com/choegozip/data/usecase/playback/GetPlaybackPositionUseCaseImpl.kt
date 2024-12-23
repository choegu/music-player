package com.choegozip.data.usecase.playback

import com.choegozip.data.repository.PlaybackRepository
import com.choegozip.domain.model.playback.PlaybackPosition
import com.choegozip.domain.usecase.playback.GetPlaybackPositionUseCase
import javax.inject.Inject

class GetPlaybackPositionUseCaseImpl @Inject constructor(
    private val playbackRepository: PlaybackRepository
): GetPlaybackPositionUseCase {

    override suspend fun invoke(): Result<PlaybackPosition> = runCatching {
        playbackRepository.getPlaybackPosition()
    }
}