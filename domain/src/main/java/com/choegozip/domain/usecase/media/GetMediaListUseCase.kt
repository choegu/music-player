package com.choegozip.domain.usecase.media

import com.choegozip.domain.model.media.Media

interface GetMediaListUseCase {
    operator fun invoke(albumId: Long): Result<List<Media>>
}