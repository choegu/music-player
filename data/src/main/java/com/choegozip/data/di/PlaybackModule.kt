package com.choegozip.data.di

import com.choegozip.data.usecase.GetPlaybackComponentUseCaseImpl
import com.choegozip.data.usecase.GetPlaybackStateUseCaseImpl
import com.choegozip.data.usecase.GetPlayerEventsUseCaseImpl
import com.choegozip.data.usecase.PlayMediaUseCaseImpl
import com.choegozip.domain.usecase.GetPlaybackComponentUseCase
import com.choegozip.domain.usecase.GetPlaybackStateUseCase
import com.choegozip.domain.usecase.GetPlayerEventsUseCase
import com.choegozip.domain.usecase.PlayMediaUseCase
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
    abstract fun bindGetPlaybackStateUseCase(uc: GetPlaybackStateUseCaseImpl): GetPlaybackStateUseCase

    @Binds
    abstract fun bindGetPlayerEventsUseCase(uc: GetPlayerEventsUseCaseImpl): GetPlayerEventsUseCase
}