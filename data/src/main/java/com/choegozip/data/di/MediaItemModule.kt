package com.choegozip.data.di

import com.choegozip.data.usecase.GetAlbumListUseCaseImpl
import com.choegozip.domain.usecase.GetAlbumListUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class MediaItemModule {
    @Binds
    abstract fun bindGetAlbumListUseCase(uc: GetAlbumListUseCaseImpl): GetAlbumListUseCase
}