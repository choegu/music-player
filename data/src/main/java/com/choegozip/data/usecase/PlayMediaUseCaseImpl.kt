package com.choegozip.data.usecase

import com.choegozip.data.repository.PlaybackRepository
import com.choegozip.domain.model.PlayMedia
import com.choegozip.domain.usecase.GetMediaListUseCase
import com.choegozip.domain.usecase.PlayMediaUseCase
import javax.inject.Inject

class PlayMediaUseCaseImpl @Inject constructor(
    private val getMediaListUseCase: GetMediaListUseCase,
    private val playbackRepository: PlaybackRepository,
): PlayMediaUseCase {

    override suspend fun invoke(playMedia: PlayMedia): Result<Unit> = runCatching {
        val mediaList = getMediaListUseCase(playMedia.albumId).getOrThrow()
        playbackRepository.playMedia(
            playMedia = playMedia,
            mediaList = mediaList
        )
    }
}