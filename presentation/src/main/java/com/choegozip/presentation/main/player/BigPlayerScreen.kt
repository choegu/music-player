package com.choegozip.presentation.main.player

import android.content.Context
import android.media.AudioManager
import android.util.Log
import android.widget.Toast
import androidx.annotation.OptIn
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.media3.ui.PlayerView
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.media3.common.util.UnstableApi
import com.choegozip.presentation.main.MainViewModel
import org.orbitmvi.orbit.compose.collectSideEffect

@OptIn(UnstableApi::class)
@Composable
fun BigPlayerScreen(
    mainViewModel: MainViewModel,
    viewModel: BigPlayerViewModel = hiltViewModel(),
    mainPlayerView: PlayerView,
    onClickFold: () -> Unit,
) {
    val context = LocalContext.current

    // 사이드이펙트 수집
    viewModel.collectSideEffect { sideEffect->
        // 예외 발생 시, 토스트로 처리
        if (sideEffect is BigPlayerSideEffect.Toast) {
            Log.d("!!!!!", "error : ${sideEffect.message}")
            Toast.makeText(context, sideEffect.message, Toast.LENGTH_SHORT).show()
        }
        // 볼륨 컨트롤러 띄우기
        if (sideEffect == BigPlayerSideEffect.ShowVolumeControl) {
            val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
            audioManager.adjustVolume(AudioManager.ADJUST_SAME, AudioManager.FLAG_SHOW_UI)
        }
    }

    BigPlayerScreen(
        mainPlayerView = mainPlayerView,
        onClickFold = onClickFold,
        onClickVolume = viewModel::onClickVolume,
    )
}

@Composable
fun BigPlayerScreen(
    mainPlayerView: PlayerView,
    onClickFold: () -> Unit,
    onClickVolume: () -> Unit,
) {
    Surface(
        color = Color.Transparent
    ) {
        Column {
            // 반투명 레이어
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .background(
                        MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
                    )
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) {
                        onClickFold()
                    }
            )

            Surface {
                Column {
                    // 상단바
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(
                                min = 64.dp
                            )
                    ) {
                        IconButton(
                            modifier = Modifier
                                .size(64.dp)
                                .align(Alignment.CenterStart),
                            onClick = onClickFold
                        ) {
                            Icon(
                                imageVector = Icons.Default.KeyboardArrowDown,
                                contentDescription = "Down",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .align(Alignment.Center),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Build the Levees",
                                style = MaterialTheme.typography.titleLarge,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = "Daniel Duke — Brother",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.secondary
                            )
                        }

                        IconButton(
                            modifier = Modifier
                                .size(64.dp)
                                .align(Alignment.CenterEnd),
                            onClick = {
                                Log.d("!!!!!", "onclick volume")
                                onClickVolume()
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = "Volume",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }

                    // PlayerView
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(5f / 4f) // 폭과 높이 비율을 5:4로 설정
                    ) {
                        AndroidView(
                            modifier = Modifier.fillMaxSize(),
                            factory = {
                                mainPlayerView
                            }
                        )
                    }
                }
            }
        }
    }
}
