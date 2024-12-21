package com.choegozip.domain.usecase

import com.choegozip.domain.model.Media

interface PlayMediaUseCase {
    suspend operator fun invoke(mediaList: List<Media>): Result<Unit>
}