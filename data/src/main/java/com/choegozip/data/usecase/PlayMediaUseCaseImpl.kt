package com.choegozip.data.usecase

import com.choegozip.data.repository.PlaybackRepository
import com.choegozip.domain.model.Media
import com.choegozip.domain.usecase.PlayMediaUseCase
import javax.inject.Inject

class PlayMediaUseCaseImpl @Inject constructor(
    private val playbackRepository: PlaybackRepository
): PlayMediaUseCase {

    override suspend fun invoke(mediaList: List<Media>): Result<Unit> = runCatching {
        playbackRepository.playMedia(mediaList)
    }
}