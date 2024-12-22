package com.choegozip.domain.usecase

import com.choegozip.domain.model.PlaybackPosition
import kotlinx.coroutines.flow.MutableSharedFlow

interface GetPositionChangedUseCase {
    suspend operator fun invoke(sharedFlow: MutableSharedFlow<PlaybackPosition>): Result<Unit>
}