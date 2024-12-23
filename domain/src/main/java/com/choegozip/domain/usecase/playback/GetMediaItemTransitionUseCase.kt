package com.choegozip.domain.usecase.playback

import com.choegozip.domain.model.media.Media
import kotlinx.coroutines.flow.MutableSharedFlow

interface GetMediaItemTransitionUseCase {
    suspend operator fun invoke(sharedFlow: MutableSharedFlow<Media>): Result<Unit>
}