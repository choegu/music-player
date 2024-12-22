package com.choegozip.data.di

import com.choegozip.data.usecase.GetMediaItemTransitionUseCaseImpl
import com.choegozip.data.usecase.GetPlayWhenReadyUseCaseImpl
import com.choegozip.data.usecase.GetPlaybackComponentUseCaseImpl
import com.choegozip.data.usecase.GetPlaybackPositionUseCaseImpl
import com.choegozip.data.usecase.GetPositionChangedUseCaseImpl
import com.choegozip.data.usecase.PlayMediaUseCaseImpl
import com.choegozip.data.usecase.PlayOrPauseMediaUseCaseImpl
import com.choegozip.data.usecase.ReleaseMediaControllerUseCaseImpl
import com.choegozip.domain.usecase.GetMediaItemTransitionUseCase
import com.choegozip.domain.usecase.GetPlayWhenReadyUseCase
import com.choegozip.domain.usecase.GetPlaybackComponentUseCase
import com.choegozip.domain.usecase.GetPlaybackPositionUseCase
import com.choegozip.domain.usecase.GetPositionChangedUseCase
import com.choegozip.domain.usecase.PlayMediaUseCase
import com.choegozip.domain.usecase.PlayOrPauseMediaUseCase
import com.choegozip.domain.usecase.ReleaseMediaControllerUseCase
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