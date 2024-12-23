package com.choegozip.domain.usecase.playback

import com.choegozip.domain.model.ComponentInfo

interface GetPlaybackComponentUseCase {
    suspend operator fun invoke(uiComponentInfo: ComponentInfo): Result<ComponentInfo>
}