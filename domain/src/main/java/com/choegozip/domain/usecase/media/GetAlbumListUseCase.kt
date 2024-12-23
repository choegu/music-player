package com.choegozip.domain.usecase.media

import com.choegozip.domain.model.media.Album

interface GetAlbumListUseCase {
    operator fun invoke(): Result<List<Album>>
}