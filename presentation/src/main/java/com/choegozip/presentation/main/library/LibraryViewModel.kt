package com.choegozip.presentation.main.library

import androidx.lifecycle.ViewModel
import com.choegozip.domain.usecase.GetAlbumListUseCase
import com.choegozip.presentation.main.MainSideEffect
import com.choegozip.presentation.model.AlbumUiModel
import com.choegozip.presentation.model.toUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class LibraryViewModel @Inject constructor(
    private val getAlbumListUseCase: GetAlbumListUseCase,
) : ViewModel(), ContainerHost<LibraryState, LibrarySideEffect> {

    override val container: Container<LibraryState, LibrarySideEffect> = container(
        initialState = LibraryState(),
        buildSettings = {
            this.exceptionHandler = CoroutineExceptionHandler { _, throwable ->
                intent { postSideEffect(LibrarySideEffect.Toast(throwable.message.orEmpty())) }
            }
        }
    )

    init {
        getAlbumList()
    }

    /**
     * 앨범 리스트 가져오기
     */
    private fun getAlbumList() = intent {
        val albumList = getAlbumListUseCase().getOrThrow().map {
            it.toUiModel()
        }

        reduce {
            state.copy(
                albumList = albumList
            )
        }
    }

    /**
     * 앨범 선택
     */
    fun onClickAlbum(album: AlbumUiModel) = intent {
        postSideEffect(LibrarySideEffect.NavigateToAlbumScreen(album))
    }
}

data class LibraryState(
    // 전체 앨범 리스트
    val albumList: List<AlbumUiModel> = emptyList(),
)

sealed interface LibrarySideEffect {
    class Toast(val message: String) : LibrarySideEffect
    class NavigateToAlbumScreen(val album: AlbumUiModel) : LibrarySideEffect
}