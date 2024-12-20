package com.choegozip.data.usecase

import com.choegozip.data.repository.MediaItemRepository
import com.choegozip.domain.model.Album
import com.choegozip.domain.usecase.GetAlbumListUseCase
import javax.inject.Inject

class GetAlbumListUseCaseImpl @Inject constructor(
    private val mediaItemRepository: MediaItemRepository
): GetAlbumListUseCase {
    override fun invoke(): Result<List<Album>> = runCatching {
        val mediaList = mediaItemRepository.getAllMusicAsMediaItems()
        mediaItemRepository.createAlbumList(mediaList)
    }
}