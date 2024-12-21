package com.choegozip.data.usecase

import com.choegozip.data.repository.MediaItemRepository
import com.choegozip.domain.model.Media
import com.choegozip.domain.usecase.GetMediaListUseCase
import javax.inject.Inject

class GetMediaListUseCaseImpl @Inject constructor(
    private val mediaItemRepository: MediaItemRepository
): GetMediaListUseCase {
    override fun invoke(albumId: Long): Result<List<Media>> = runCatching {
        mediaItemRepository.getMediaListByAlbum(albumId)
    }
}