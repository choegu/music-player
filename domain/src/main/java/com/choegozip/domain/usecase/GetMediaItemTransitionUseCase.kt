package com.choegozip.domain.usecase

import com.choegozip.domain.model.Media
import kotlinx.coroutines.flow.MutableSharedFlow

interface GetMediaItemTransitionUseCase {
    suspend operator fun invoke(sharedFlow: MutableSharedFlow<Media>): Result<Unit>
}