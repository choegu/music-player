package com.choegozip.data.usecase.media

import com.choegozip.data.repository.MediaItemRepository
import com.choegozip.domain.model.media.Album
import com.choegozip.domain.usecase.media.GetAlbumListUseCase
import javax.inject.Inject

class GetAlbumListUseCaseImpl @Inject constructor(
    private val mediaItemRepository: MediaItemRepository
): GetAlbumListUseCase {
    override fun invoke(): Result<List<Album>> = runCatching {
        val mediaList = mediaItemRepository.getAllMusicAsMediaItems()

        // TODO 미디어 리스트는 제외하고 전송하도록 리팩토링
        mediaItemRepository.createAlbumList(mediaList)
    }
}