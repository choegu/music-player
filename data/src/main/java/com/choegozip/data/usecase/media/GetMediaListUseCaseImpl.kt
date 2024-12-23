package com.choegozip.data.usecase.media

import com.choegozip.data.repository.MediaItemRepository
import com.choegozip.domain.model.media.Media
import com.choegozip.domain.usecase.media.GetMediaListUseCase
import javax.inject.Inject

class GetMediaListUseCaseImpl @Inject constructor(
    private val mediaItemRepository: MediaItemRepository
): GetMediaListUseCase {
    override fun invoke(albumId: Long): Result<List<Media>> = runCatching {
        mediaItemRepository.getMediaListByAlbum(albumId)
    }
}