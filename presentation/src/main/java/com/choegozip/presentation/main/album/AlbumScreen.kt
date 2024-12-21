package com.choegozip.presentation.main.album

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.media3.common.util.Log
import coil.compose.rememberAsyncImagePainter
import com.choegozip.domain.model.PlayMedia
import com.choegozip.presentation.main.MainViewModel
import com.choegozip.presentation.model.MediaUiModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun AlbumScreen(
    mainViewModel: MainViewModel,
    viewModel: AlbumViewModel = hiltViewModel(),
    albumId: Long,
) {
    val state = viewModel.collectAsState().value
    val context = LocalContext.current

    // 사이드이펙트 수집
    viewModel.collectSideEffect { sideEffect->
        // 예외 발생 시, 토스트로 처리
        if (sideEffect is AlbumSideEffect.Toast) {
            Log.d("!!!!!", "error : ${sideEffect.message}")
            Toast.makeText(context, sideEffect.message, Toast.LENGTH_SHORT).show()
        }
    }

    // 미디어 리스트 가져오기
    viewModel.getMediaList(albumId)

    AlbumScreen(
        albumId = albumId,
        mediaList = state.mediaList,
        onPlayMedia = viewModel::playMedia
    )
}

@Composable
private fun AlbumScreen(
    albumId: Long,
    mediaList: List<MediaUiModel>,
    onPlayMedia: (PlayMedia) -> Unit
) {
    Surface {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(
                    horizontal = 16.dp
                )
        ) {
            mediaList.firstOrNull()?.run {
                // 앨범 아트와 제목, 아티스트 이름
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            top = 16.dp
                        )
                ) {
                    Image(
                        painter = rememberAsyncImagePainter(model = albumArtUri),
                        contentDescription = "Album Art",
                        modifier = Modifier
                            .size(64.dp)
                            .padding(end = 16.dp),
                        contentScale = ContentScale.Crop
                    )
                    Column {
                        Text(
                            text = albumTitle,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = artist,
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 재생 버튼과 셔플 버튼 TODO 간격 등 조금 더 예쁘게 변경
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = {
                        onPlayMedia(
                            PlayMedia(
                                albumId = albumId,
                                mediaIndex = null,
                                shuffleModeEnabled = false
                            )
                        )
                    },
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "Play",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }

                Button(
                    onClick = {
                        onPlayMedia(
                            PlayMedia(
                                albumId = albumId,
                                mediaIndex = null,
                                shuffleModeEnabled = true
                            )
                        )
                    },
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary
                    )
                ) {
                    // TODO 이미지 어울리는 것으로 변경
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = "Play",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 곡 목록 리스트
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth(),
                contentPadding = PaddingValues(
                    top = 16.dp,
                    bottom = 16.dp
                ),
            ) {
                itemsIndexed(mediaList) { index, media ->
                    MediaItemRow(
                        media = media,
                        index = index,
                        onMediaItemClick = {
                            onPlayMedia(
                                PlayMedia(
                                    albumId = albumId,
                                    mediaIndex = index,
                                    shuffleModeEnabled = false
                                )
                            )
                        }
                    )
                }
            }
        }
    }
}
