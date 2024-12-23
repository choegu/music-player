package com.choegozip.domain.usecase.playback

import com.choegozip.domain.model.playback.PlaybackPosition
import kotlinx.coroutines.flow.MutableSharedFlow

interface GetPositionChangedUseCase {
    suspend operator fun invoke(sharedFlow: MutableSharedFlow<PlaybackPosition>): Result<Unit>
}