package com.choegozip.data.usecase.playback

import com.choegozip.data.repository.PlaybackRepository
import com.choegozip.domain.model.media.Media
import com.choegozip.domain.usecase.playback.GetMediaItemTransitionUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import javax.inject.Inject

class GetMediaItemTransitionUseCaseImpl @Inject constructor(
    private val playbackRepository: PlaybackRepository
): GetMediaItemTransitionUseCase {

    override suspend fun invoke(sharedFlow: MutableSharedFlow<Media>): Result<Unit> = runCatching {
        playbackRepository.getMediaItemTransition(sharedFlow)
    }
}