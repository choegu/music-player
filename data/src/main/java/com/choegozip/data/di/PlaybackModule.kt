package com.choegozip.data.di

import com.choegozip.data.usecase.playback.GetMediaItemTransitionUseCaseImpl
import com.choegozip.data.usecase.playback.GetPlayWhenReadyUseCaseImpl
import com.choegozip.data.usecase.playback.GetPlaybackComponentUseCaseImpl
import com.choegozip.data.usecase.playback.GetPlaybackPositionUseCaseImpl
import com.choegozip.data.usecase.playback.GetPositionChangedUseCaseImpl
import com.choegozip.data.usecase.playback.PlayMediaUseCaseImpl
import com.choegozip.data.usecase.playback.PlayOrPauseMediaUseCaseImpl
import com.choegozip.data.usecase.playback.ReleaseMediaControllerUseCaseImpl
import com.choegozip.domain.usecase.playback.GetMediaItemTransitionUseCase
import com.choegozip.domain.usecase.playback.GetPlayWhenReadyUseCase
import com.choegozip.domain.usecase.playback.GetPlaybackComponentUseCase
import com.choegozip.domain.usecase.playback.GetPlaybackPositionUseCase
import com.choegozip.domain.usecase.playback.GetPositionChangedUseCase
import com.choegozip.domain.usecase.playback.PlayMediaUseCase
import com.choegozip.domain.usecase.playback.PlayOrPauseMediaUseCase
import com.choegozip.domain.usecase.playback.ReleaseMediaControllerUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class PlaybackModule {
    @Binds
    abstract fun bindGetPlaybackComponentUseCase(uc: GetPlaybackComponentUseCaseImpl): GetPlaybackComponentUseCase

    @Binds
    abstract fun bindPlayMediaUseCaseUseCase(uc: PlayMediaUseCaseImpl): PlayMediaUseCase

    @Binds
    abstract fun bindGetPlaybackStateUseCase(uc: GetPlaybackPositionUseCaseImpl): GetPlaybackPositionUseCase

    @Binds
    abstract fun bindGetPlayWhenReadyUseCase(uc: GetPlayWhenReadyUseCaseImpl): GetPlayWhenReadyUseCase

    @Binds
    abstract fun bindGetPositionChangedUseCase(uc: GetPositionChangedUseCaseImpl): GetPositionChangedUseCase

    @Binds
    abstract fun bindGetMediaItemTransitionUseCase(uc: GetMediaItemTransitionUseCaseImpl): GetMediaItemTransitionUseCase

    @Binds
    abstract fun bindPlayOrPauseMediaUseCase(uc: PlayOrPauseMediaUseCaseImpl): PlayOrPauseMediaUseCase

    @Binds
    abstract fun bindReleaseMediaControllerUseCase(uc: ReleaseMediaControllerUseCaseImpl): ReleaseMediaControllerUseCase
}