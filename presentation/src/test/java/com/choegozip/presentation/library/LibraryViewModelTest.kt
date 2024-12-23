package com.choegozip.presentation.library

import android.net.Uri
import com.choegozip.domain.model.media.Album
import com.choegozip.domain.usecase.media.GetAlbumListUseCase
import com.choegozip.presentation.MainDispatcherRule
import com.choegozip.presentation.main.library.LibrarySideEffect
import com.choegozip.presentation.main.library.LibraryViewModel
import com.choegozip.presentation.model.AlbumUiModel
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class LibraryViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()
    
    private lateinit var viewModel: LibraryViewModel
    private lateinit var getAlbumListUseCase: GetAlbumListUseCase
    
    @Before
    fun setup() {
        getAlbumListUseCase = mockk()
    }
    
    @Test
    fun `초기화시 앨범 목록을 가져와서 상태를 업데이트한다`() = runTest {
        // Given
        val mockAlbums = listOf(
            Album(title = "에피소드", artist = "이무진", albumId = 10000001),
            Album(title = "아파트", artist = "로제", albumId = 10000002),
        )
        coEvery { getAlbumListUseCase() } returns Result.success(mockAlbums)

        // When
        viewModel = LibraryViewModel(getAlbumListUseCase)

        // Then
        val state = viewModel.container.stateFlow.first { it.albumList.isNotEmpty() }

        assertEquals(2, state.albumList.size)
        assertEquals("에피소드", state.albumList[0].title)
    }
    
    @Test
    fun `앨범 클릭시 NavigateToAlbumScreen 사이드이펙트가 발생한다`() = runTest {
        // Given
        val album = AlbumUiModel(
            title = "앨범 테스트 타이틀",
            artist = "테스트 아티스트",
            albumId = 20000001,
            albumArtUri = Uri.EMPTY
        )
        coEvery { getAlbumListUseCase() } returns Result.success(emptyList())
        
        // When
        viewModel = LibraryViewModel(getAlbumListUseCase)
        viewModel.onClickAlbum(album)
        
        // Then
        val sideEffect = viewModel.container.sideEffectFlow.first()
        assertTrue(sideEffect is LibrarySideEffect.NavigateToAlbumScreen)
        assertEquals(album, (sideEffect as LibrarySideEffect.NavigateToAlbumScreen).album)
    }
    
    @Test
    fun `앨범 목록 가져오기 실패시 Toast 사이드이펙트가 발생한다`() = runTest {
        // Given
        val errorMessage = "Error fetching albums"
        coEvery { getAlbumListUseCase() } returns Result.failure(Exception(errorMessage))
        
        // When
        viewModel = LibraryViewModel(getAlbumListUseCase)
        
        // Then
        val sideEffect = viewModel.container.sideEffectFlow.first()
        assertTrue(sideEffect is LibrarySideEffect.Toast)
        assertEquals(errorMessage, (sideEffect as LibrarySideEffect.Toast).message)
    }
}