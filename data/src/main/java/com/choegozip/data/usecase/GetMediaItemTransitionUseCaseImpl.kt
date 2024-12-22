package com.choegozip.data.usecase

import com.choegozip.data.repository.PlaybackRepository
import com.choegozip.domain.model.Media
import com.choegozip.domain.usecase.GetMediaItemTransitionUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import javax.inject.Inject

class GetMediaItemTransitionUseCaseImpl @Inject constructor(
    private val playbackRepository: PlaybackRepository
): GetMediaItemTransitionUseCase {

    override suspend fun invoke(sharedFlow: MutableSharedFlow<Media>): Result<Unit> = runCatching {
        playbackRepository.getMediaItemTransition(sharedFlow)
    }
}