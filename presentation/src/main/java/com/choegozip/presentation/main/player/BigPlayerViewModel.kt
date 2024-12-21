package com.choegozip.presentation.main.player

import androidx.lifecycle.ViewModel
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
class BigPlayerViewModel @Inject constructor(
) : ViewModel(), ContainerHost<Unit, BigPlayerSideEffect> {

    override val container: Container<Unit, BigPlayerSideEffect> = container(
        initialState = Unit,
        buildSettings = {
            this.exceptionHandler = CoroutineExceptionHandler { _, throwable ->
                intent { postSideEffect(BigPlayerSideEffect.Toast(throwable.message.orEmpty())) }
            }
        }
    )

    /**
     * 볼륨 클릭
     */
    fun onClickVolume() = intent {
        postSideEffect(BigPlayerSideEffect.ShowVolumeControl)
    }
}

sealed interface BigPlayerSideEffect {
    class Toast(val message: String) : BigPlayerSideEffect
    data object ShowVolumeControl : BigPlayerSideEffect
}