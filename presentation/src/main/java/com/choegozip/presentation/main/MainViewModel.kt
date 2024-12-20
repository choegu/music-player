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
}

data class MainState(
    val albumList:List<AlbumUiModel> = emptyList(),
)

sealed interface MainSideEffect {
    class Toast(val message: String) : MainSideEffect
}