package com.choegozip.presentation.main.player

import androidx.lifecycle.ViewModel
import com.choegozip.domain.usecase.PlayOrPauseMediaUseCase
import com.choegozip.presentation.model.AlbumUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class SmallPlayerViewModel @Inject constructor(
    private val playOrPauseMediaUseCase: PlayOrPauseMediaUseCase
) : ViewModel(), ContainerHost<Unit, SmallPlayerSideEffect> {

    override val container: Container<Unit, SmallPlayerSideEffect> = container(
        initialState = Unit,
        buildSettings = {
            this.exceptionHandler = CoroutineExceptionHandler { _, throwable ->
                intent { postSideEffect(SmallPlayerSideEffect.Toast(throwable.message.orEmpty())) }
            }
        }
    )

    /**
     * play or pause
     */
    fun playOrPause() = intent {
        playOrPauseMediaUseCase().getOrThrow()
    }
}

sealed interface SmallPlayerSideEffect {
    class Toast(val message: String) : SmallPlayerSideEffect
}