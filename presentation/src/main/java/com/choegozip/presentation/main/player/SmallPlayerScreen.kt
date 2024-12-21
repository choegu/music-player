package com.choegozip.presentation.main.player

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.media3.common.util.Log
import com.choegozip.domain.model.PlaybackState
import com.choegozip.presentation.main.MainViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun SmallPlayerScreen(
    mainViewModel: MainViewModel,
    viewModel: SmallPlayerViewModel = hiltViewModel(),
    onClickSpread: () -> Unit,
) {
    val state = mainViewModel.collectAsState().value
    val context = LocalContext.current

    // 사이드이펙트 수집
    viewModel.collectSideEffect { sideEffect->
        // 예외 발생 시, 토스트로 처리
        if (sideEffect is SmallPlayerSideEffect.Toast) {
            Log.d("!!!!!", "error : ${sideEffect.message}")
            Toast.makeText(context, sideEffect.message, Toast.LENGTH_SHORT).show()
        }
    }

    SmallPlayerScreen(
        playbackState = state.playbackState,
        onClickSpread = onClickSpread,
    )
}

@Composable
private fun SmallPlayerScreen(
    playbackState: PlaybackState,
    onClickSpread: () -> Unit,
) {
    Surface {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(
                    min = 64.dp
                )
                .background(Color.Gray)
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) {
                    onClickSpread()
                }
        ) {
            Text(
                text = playbackState.currentPosition.toString(),
                modifier = Modifier
                    .align(Alignment.CenterStart)
            )

            Text(
                text = playbackState.duration.toString(),
                modifier = Modifier
                    .align(Alignment.CenterEnd)
            )
        }
    }
}