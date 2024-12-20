package com.choegozip.domain.usecase

import com.choegozip.domain.model.Album

interface GetAlbumListUseCase {
    operator fun invoke(): Result<List<Album>>
}