package com.choegozip.data.di

import com.choegozip.data.usecase.media.GetAlbumListUseCaseImpl
import com.choegozip.data.usecase.media.GetMediaListUseCaseImpl
import com.choegozip.domain.usecase.media.GetAlbumListUseCase
import com.choegozip.domain.usecase.media.GetMediaListUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class MediaItemModule {
    @Binds
    abstract fun bindGetAlbumListUseCase(uc: GetAlbumListUseCaseImpl): GetAlbumListUseCase

    @Binds
    abstract fun bindGetMediaListUseCase(uc: GetMediaListUseCaseImpl): GetMediaListUseCase
}