package com.choegozip.data.usecase.playback

import com.choegozip.data.repository.PlaybackRepository
import com.choegozip.domain.model.playback.PlaybackPosition
import com.choegozip.domain.usecase.playback.GetPositionChangedUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import javax.inject.Inject

class GetPositionChangedUseCaseImpl @Inject constructor(
    private val playbackRepository: PlaybackRepository
): GetPositionChangedUseCase {

    override suspend fun invoke(sharedFlow: MutableSharedFlow<PlaybackPosition>): Result<Unit> = runCatching {
        playbackRepository.getPositionChanged(sharedFlow)
    }
}