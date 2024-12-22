package com.choegozip.presentation.main.player

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.media3.common.util.Log
import coil.compose.rememberAsyncImagePainter
import com.choegozip.domain.model.PlaybackPosition
import com.choegozip.presentation.main.MainViewModel
import com.choegozip.presentation.model.MediaUiModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun SmallPlayerScreen(
    mainViewModel: MainViewModel,
    viewModel: SmallPlayerViewModel = hiltViewModel(),
    onClickSpread: () -> Unit,
) {
    val mainState = mainViewModel.collectAsState().value
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
        playerWhenReady = mainState.playWhenReady.collectAsState(initial = false).value,
        playbackPosition = mainState.positionChanged.collectAsState(initial = PlaybackPosition.empty()).value,
        mediaUiModel = mainState.mediaItemTransition.collectAsState(initial = MediaUiModel.empty()).value,
        onClickSpread = onClickSpread,
        onPlayPauseClicked = viewModel::playOrPause,
    )
}

@Composable
private fun SmallPlayerScreen(
    playerWhenReady: Boolean,
    playbackPosition: PlaybackPosition,
    mediaUiModel: MediaUiModel,
    onClickSpread: () -> Unit,
    onPlayPauseClicked: () -> Unit,
) {

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .offset(y = (-8).dp),
    ) {
        // TODO 상단 여백이 좁은데 알맞게 수정 필요
        Column(
            modifier = Modifier
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) {
                    onClickSpread()
                }
        ) {
            // 진행바
            LinearProgressIndicator(
                progress = { playbackPosition.getProgress() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp),
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f),
            )

            if (mediaUiModel.isEmpty()) {
                // TODO 문자열 리소스화
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(64.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "재생중인 노래가 없습니다.",
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.primary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            } else {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(64.dp)
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Play / Pause 버튼
                    IconButton(onClick = onPlayPauseClicked) {
                        // TODO pause 아이콘 어울리는 걸로 변경
                        Icon(
                            imageVector = if (playerWhenReady) Icons.Default.Menu else Icons.Default.PlayArrow,
                            contentDescription = "Play/Pause",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(48.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    // 텍스트 정보
                    Column(
                        modifier = Modifier
                            .weight(1f),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Text(
                            text = mediaUiModel.mediaTitle,
                            style = MaterialTheme.typography.titleSmall,
                            color = MaterialTheme.colorScheme.primary,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = "${mediaUiModel.artist} - ${mediaUiModel.albumTitle}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.secondary,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    // 앨범 아트
                    Image(
                        painter = rememberAsyncImagePainter(mediaUiModel.albumArtUri),
                        contentDescription = "Album Art",
                        modifier = Modifier
                            .size(48.dp),
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }
    }
}