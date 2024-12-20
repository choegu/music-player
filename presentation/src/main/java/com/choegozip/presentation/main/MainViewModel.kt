package com.choegozip.presentation.main

import androidx.lifecycle.ViewModel
import com.choegozip.domain.usecase.GetAlbumListUseCase
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
class MainViewModel @Inject constructor(
    private val getAlbumListUseCase: GetAlbumListUseCase,
) : ViewModel(), ContainerHost<MainState, MainSideEffect> {

    override val container: Container<MainState, MainSideEffect> = container(
        initialState = MainState(),
        buildSettings = {
            this.exceptionHandler = CoroutineExceptionHandler { _, throwable ->
                intent { postSideEffect(MainSideEffect.Toast(throwable.message.orEmpty())) }
            }
        }
    )

    init {
        load()
    }

    fun load() = intent {
        val albumList = getAlbumListUseCase().getOrThrow().map {
            it.toUiModel()
        }

        reduce {
            state.copy(
                albumList = albumList
            )
        }
    }

    fun onSelectAlbum(album: AlbumUiModel) = intent {
        reduce {
            state.copy(
                selectedAlbum = album
            )
        }

        postSideEffect(MainSideEffect.NavigateToAlbumScreen)
    }
}

data class MainState(
    // 전체 앨범 리스트
    val albumList: List<AlbumUiModel> = emptyList(),
    // 앨범 스크린 이동을 위한 선택한 앨범
    val selectedAlbum: AlbumUiModel? = null,
)

sealed interface MainSideEffect {
    class Toast(val message: String) : MainSideEffect
    data object NavigateToAlbumScreen : MainSideEffect
}