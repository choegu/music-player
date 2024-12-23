package com.choegozip.domain.usecase.playback

import kotlinx.coroutines.flow.MutableSharedFlow

interface GetPlayWhenReadyUseCase {
    suspend operator fun invoke(sharedFlow: MutableSharedFlow<Boolean>): Result<Unit>
}