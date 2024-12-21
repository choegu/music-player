package com.choegozip.domain.usecase

import com.choegozip.domain.model.PlayMedia

interface PlayMediaUseCase {
    suspend operator fun invoke(playMedia: PlayMedia): Result<Unit>
}