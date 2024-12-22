package com.choegozip.presentation.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.choegozip.domain.model.ComponentInfo
import com.choegozip.domain.model.Media
import com.choegozip.domain.model.PlaybackPosition
import com.choegozip.domain.usecase.GetMediaItemTransitionUseCase
import com.choegozip.domain.usecase.GetPlayWhenReadyUseCase
import com.choegozip.domain.usecase.GetPlaybackComponentUseCase
import com.choegozip.domain.usecase.GetPlaybackPositionUseCase
import com.choegozip.domain.usecase.GetPositionChangedUseCase
import com.choegozip.domain.usecase.ReleaseMediaControllerUseCase
import com.choegozip.presentation.model.MediaUiModel
import com.choegozip.presentation.model.toUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getPlaybackComponentUseCase: GetPlaybackComponentUseCase,
    private val getPlaybackPositionUseCase: GetPlaybackPositionUseCase,
    private val getPlayWhenReadyUseCase: GetPlayWhenReadyUseCase,
    private val getPositionChangedUseCase: GetPositionChangedUseCase,
    private val getMediaItemTransitionUseCase: GetMediaItemTransitionUseCase,
    private val releaseMediaControllerUseCase: ReleaseMediaControllerUseCase,
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
        observePlayWhenReady()
    }

    // PlayWhenReady 플로우
    private val _playWhenReadyFlow = MutableSharedFlow<Boolean>(replay = 1)
    private val playWhenReadyFlow: Flow<Boolean> = _playWhenReadyFlow

    // PositionChanged 플로우
    private val _positionChangedFlow = MutableSharedFlow<PlaybackPosition>(replay = 1)
    private val positionChangedFlow: Flow<PlaybackPosition> = _positionChangedFlow

    // MediaItemTransition 플로우
    private val _mediaItemTransitionFlow = MutableSharedFlow<Media>(replay = 1)
    private val mediaItemTransitionFlow: Flow<Media> = _mediaItemTransitionFlow

    // Position update job
    private var positionUpdateJob: Job? = null

    /**
     * 재생 상태 관찰 및 코루틴 관리
     */
    private fun observePlayWhenReady() {
        viewModelScope.launch {
            playWhenReadyFlow.collect { playWhenReady ->
                if (playWhenReady) {
                    startPositionUpdate()
                } else {
                    stopPositionUpdate()
                }
            }
        }
    }

    /**
     * 포지션 업데이트 시작
     */
    private fun startPositionUpdate() {
        if (positionUpdateJob?.isActive == true) return

        positionUpdateJob = viewModelScope.launch {
            while (isActive) {
                delay(500)
                val playbackPosition = getPlaybackPositionUseCase().getOrThrow()
                _positionChangedFlow.tryEmit(playbackPosition)
            }
        }
    }

    /**
     * 포지션 업데이트 종료
     */
    private fun stopPositionUpdate() {
        positionUpdateJob?.cancel()
        positionUpdateJob = null
    }

    /**
     * 재생 컴포넌트 정보 가져오기
     */
    fun getPlaybackComponent(uiComponentInfo: ComponentInfo) = intent {
        // 재생 컴포넌트 정보 가져오기
        val playbackComponentInfo = getPlaybackComponentUseCase(uiComponentInfo).getOrThrow()

        // 플레이어 이벤트 리스너 붙이기
        getPlayWhenReadyUseCase(_playWhenReadyFlow).getOrThrow()
        getPositionChangedUseCase(_positionChangedFlow).getOrThrow()
        getMediaItemTransitionUseCase(_mediaItemTransitionFlow).getOrThrow()

        reduce {
            state.copy(
                playbackComponentInfo = playbackComponentInfo,
                playWhenReady = playWhenReadyFlow,
                positionChanged = positionChangedFlow,
                mediaItemTransition = mediaItemTransitionFlow.map { it.toUiModel() },
            )
        }
    }

    /**
     * 미디어 플레이어 종료
     */
    fun releaseMediaController() = intent {
        releaseMediaControllerUseCase().getOrThrow()
    }
}

data class MainState(
    val playbackComponentInfo: ComponentInfo = ComponentInfo.empty(),
    val playWhenReady: Flow<Boolean> = emptyFlow(),
    val positionChanged: Flow<PlaybackPosition> = emptyFlow(),
    val mediaItemTransition: Flow<MediaUiModel> = emptyFlow(),
)

sealed interface MainSideEffect {
    class Toast(val message: String) : MainSideEffect
}