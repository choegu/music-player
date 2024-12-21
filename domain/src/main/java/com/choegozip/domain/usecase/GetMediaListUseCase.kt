package com.choegozip.domain.usecase

import com.choegozip.domain.model.Media

interface GetMediaListUseCase {
    operator fun invoke(albumId: Long): Result<List<Media>>
}