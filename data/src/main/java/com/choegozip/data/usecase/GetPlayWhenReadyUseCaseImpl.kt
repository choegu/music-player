package com.choegozip.data.usecase

import com.choegozip.data.repository.PlaybackRepository
import com.choegozip.domain.usecase.GetPlayWhenReadyUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import javax.inject.Inject

class GetPlayWhenReadyUseCaseImpl @Inject constructor(
    private val playbackRepository: PlaybackRepository
): GetPlayWhenReadyUseCase {

    override suspend fun invoke(sharedFlow: MutableSharedFlow<Boolean>): Result<Unit> = runCatching {
        playbackRepository.getPlayWhenReady(sharedFlow)
    }
}