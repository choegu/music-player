package com.choegozip.presentation.main

import android.content.ComponentName
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.media3.common.util.RepeatModeUtil.REPEAT_TOGGLE_MODE_ALL
import androidx.media3.common.util.RepeatModeUtil.REPEAT_TOGGLE_MODE_ONE
import androidx.media3.common.util.UnstableApi
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import androidx.media3.ui.PlayerView
import androidx.media3.ui.PlayerView.ARTWORK_DISPLAY_MODE_FIT
import com.choegozip.domain.model.ComponentInfo
import com.choegozip.presentation.theme.MusicPlayerTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.guava.await
import kotlinx.coroutines.launch

@UnstableApi
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    // 메인 통합 뷰모델
    // 여러 스크린에서 통합 사용하기 위해, hiltViewModel 이 아닌, 일반적인 방법으로 생성
    private val mainViewModel: MainViewModel by viewModels()

    // 메인 미디어 플레이어
    private val mainPlayerView by lazy {
        PlayerView(applicationContext).apply {
            setShowShuffleButton(true)
            setRepeatToggleModes(REPEAT_TOGGLE_MODE_ONE or REPEAT_TOGGLE_MODE_ALL)
            artworkDisplayMode = ARTWORK_DISPLAY_MODE_FIT
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 상태 관찰
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                mainViewModel.container.stateFlow.collect { state ->
                }
            }
        }

        // 사이드 이펙트 관찰
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                mainViewModel.container.sideEffectFlow.collect { sideEffect ->
                    when (sideEffect) {
                        is MainSideEffect.Toast -> {
                            Log.d("!!!!!", "error : ${sideEffect.message}")
                            Toast.makeText(applicationContext, sideEffect.message, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }

        // 재생 컴포넌트 요청
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                try {
                    MainActivity::class.java.canonicalName?.let { className ->
                        mainViewModel.getPlaybackComponent(
                            ComponentInfo(
                                packageName = packageName,
                                className = className
                            )
                        )
                    }
                    awaitCancellation()
                } catch (e: Exception) {
                    // TODO 예외 처리

                } finally {
                    // TODO 릴리즈 컨트롤러
                }
            }
        }

        // 재생 컴포넌트 감지
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                mainViewModel
                    .container
                    .stateFlow
                    .map { it.playbackComponentInfo }
                    .distinctUntilChanged()
                    .collect { playbackComponentInfo ->
                        if (!playbackComponentInfo.isEmpty()) {
                            createAndConnectPlayer(playbackComponentInfo)
                        }
                    }
            }
        }

        // 주기적으로 현재 미디어 상태 요청
//        lifecycleScope.launch {
//            repeatOnLifecycle(Lifecycle.State.RESUMED) {
//                mainViewModel.startGetPlaybackState()
//            }
//        }

        setContent {
            MusicPlayerTheme {
                MainNavHost(
                    mainViewModel = mainViewModel,
                    mainPlayerView = mainPlayerView
                )
            }
        }

    }

    /**
     * 플레이어 생성 및 연결
     */
    private suspend fun createAndConnectPlayer(playbackComponentInfo: ComponentInfo) {
        Log.d("!!!!!", "createAndConnectPlayer : $playbackComponentInfo")

        // 재생 컴포넌트 정보를 이용하여
        val playbackComponent = ComponentName(playbackComponentInfo.packageName, playbackComponentInfo.className)
        val sessionToken = SessionToken(this, playbackComponent)
        val controller = MediaController.Builder(
            this,
            sessionToken,
        ).buildAsync().await()

        mainPlayerView.player = controller
    }
}