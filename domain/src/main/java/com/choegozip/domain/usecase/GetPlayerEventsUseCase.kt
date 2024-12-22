package com.choegozip.domain.usecase

import com.choegozip.domain.model.PlayerEvent
import kotlinx.coroutines.flow.MutableSharedFlow

interface GetPlayerEventsUseCase {
    suspend operator fun invoke(sharedFlow: MutableSharedFlow<PlayerEvent>): Result<Unit>
}