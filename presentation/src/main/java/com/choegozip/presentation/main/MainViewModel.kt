package com.choegozip.presentation.main

import androidx.lifecycle.ViewModel
import com.choegozip.domain.model.ComponentInfo
import com.choegozip.domain.model.PlaybackState
import com.choegozip.domain.model.PlayerEvent
import com.choegozip.domain.usecase.GetPlaybackComponentUseCase
import com.choegozip.domain.usecase.GetPlaybackStateUseCase
import com.choegozip.domain.usecase.GetPlayerEventsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.emptyFlow
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
    private val getPlaybackStateUseCase: GetPlaybackStateUseCase,
    private val getPlayerEventsUseCase: GetPlayerEventsUseCase,
) : ViewModel(), ContainerHost<MainState, MainSideEffect> {

    override val container: Container<MainState, MainSideEffect> = container(
        initialState = MainState(),
        buildSettings = {
            this.exceptionHandler = CoroutineExceptionHandler { _, throwable ->
                intent { postSideEffect(MainSideEffect.Toast(throwable.message.orEmpty())) }
            }
        }
    )

    // 플레이어 이벤트 플로우
    private val _playerEventsFlow = MutableSharedFlow<PlayerEvent>(replay = 1)
    private val playerEventsFlow: Flow<PlayerEvent> = _playerEventsFlow

    /**
     * 재생 컴포넌트 정보 가져오기
     */
    fun getPlaybackComponent(uiComponentInfo: ComponentInfo) = intent {
        // 재생 컴포넌트 정보 가져오기
        val playbackComponentInfo = getPlaybackComponentUseCase(uiComponentInfo).getOrThrow()

        // 플레이어 이벤트 리스너 붙이기
        getPlayerEventsUseCase(_playerEventsFlow).getOrThrow()

        reduce {
            state.copy(
                playbackComponentInfo = playbackComponentInfo,
                playerEventFlow = playerEventsFlow,
            )
        }
    }

    /**
     * 미디어 상태 수집 시작
     */
    fun startGetPlaybackState() = intent {
        while (true) {
            // TODO 현재 재생중일 때만 동작하도록 변경

            // 반복적으로 수집
            delay(1000)

            val playbackState = getPlaybackStateUseCase().getOrThrow()

            reduce {
                state.copy(
                    playbackState = playbackState
                )
            }
        }
    }
}

data class MainState(
    // 디바이스 재생 컴포넌트 정보
    val playbackComponentInfo: ComponentInfo = ComponentInfo.empty(),
    // 플레이어 이벤트
    val playerEventFlow: Flow<PlayerEvent> = emptyFlow(),
    // 재생 상태
    val playbackState: PlaybackState = PlaybackState.empty(),
)

sealed interface MainSideEffect {
    class Toast(val message: String) : MainSideEffect
}