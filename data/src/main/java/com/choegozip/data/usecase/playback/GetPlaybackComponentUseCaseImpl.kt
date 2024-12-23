package com.choegozip.data.usecase.playback

import com.choegozip.data.repository.PlaybackRepository
import com.choegozip.domain.model.ComponentInfo
import com.choegozip.domain.usecase.playback.GetPlaybackComponentUseCase
import javax.inject.Inject

class GetPlaybackComponentUseCaseImpl @Inject constructor(
    private val playbackRepository: PlaybackRepository
): GetPlaybackComponentUseCase {

    override suspend fun invoke(uiComponentInfo: ComponentInfo): Result<ComponentInfo> = runCatching {
        playbackRepository.getPlaybackComponent(uiComponentInfo)
    }
}