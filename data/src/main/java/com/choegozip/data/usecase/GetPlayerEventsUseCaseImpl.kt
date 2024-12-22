package com.choegozip.data.usecase

import com.choegozip.data.repository.PlaybackRepository
import com.choegozip.domain.model.PlayerEvent
import com.choegozip.domain.usecase.GetPlayerEventsUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import javax.inject.Inject

class GetPlayerEventsUseCaseImpl @Inject constructor(
    private val playbackRepository: PlaybackRepository
): GetPlayerEventsUseCase {

    override suspend fun invoke(sharedFlow: MutableSharedFlow<PlayerEvent>): Result<Unit> = runCatching {
        playbackRepository.getPlayerEvents(sharedFlow)
    }
}