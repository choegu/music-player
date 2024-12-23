package com.choegozip.data.usecase.playback

import com.choegozip.data.repository.PlaybackRepository
import com.choegozip.domain.model.playback.PlayMedia
import com.choegozip.domain.usecase.media.GetMediaListUseCase
import com.choegozip.domain.usecase.playback.PlayMediaUseCase
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