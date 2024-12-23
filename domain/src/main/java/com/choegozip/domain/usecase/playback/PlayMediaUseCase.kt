package com.choegozip.domain.usecase.playback

import com.choegozip.domain.model.playback.PlayMedia

interface PlayMediaUseCase {
    suspend operator fun invoke(playMedia: PlayMedia): Result<Unit>
}