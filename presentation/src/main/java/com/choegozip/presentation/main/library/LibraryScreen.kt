package com.choegozip.presentation.main.library

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.annotation.OptIn
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.media3.common.util.Log
import androidx.media3.common.util.UnstableApi
import com.choegozip.presentation.main.MainViewModel
import com.choegozip.presentation.model.AlbumUiModel
import com.choegozip.presentation.theme.MusicPlayerTheme
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@OptIn(UnstableApi::class)
@Composable
fun LibraryScreen(
    mainViewModel: MainViewModel,
    viewModel: LibraryViewModel = hiltViewModel(),
    onNavigateToAlbumScreen: (AlbumUiModel) -> Unit,
) {
    val mainState = mainViewModel.collectAsState().value
    val state = viewModel.collectAsState().value
    val context = LocalContext.current

    // 사이드이펙트 수집
    viewModel.collectSideEffect { sideEffect->
        when (sideEffect) {
            is LibrarySideEffect.Toast -> {
                Log.d("!!!!!", "error : ${sideEffect.message}")
                Toast.makeText(context, sideEffect.message, Toast.LENGTH_SHORT).show()
            }
            is LibrarySideEffect.NavigateToAlbumScreen -> {
                onNavigateToAlbumScreen(sideEffect.album)
            }
        }
    }

    LibraryScreen(
        isExpanded = mainState.isExpanded,
        toggleExpand = mainViewModel::toggleExpanded,
        albumList = state.albumList,
        onAlbumClick = viewModel::onClickAlbum
    )
}

@Composable
private fun LibraryScreen(
    isExpanded: Boolean,
    toggleExpand: () -> Unit,
    albumList: List<AlbumUiModel>,
    onAlbumClick: (AlbumUiModel) -> Unit,
) {
    Surface {
        Crossfade(
            targetState = albumList,
            label = "LibraryScreen AlbumList Crossfade",
            animationSpec = tween(
                durationMillis = 500
            )
        ) { targetState ->
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(16.dp),
                modifier = Modifier
                    .fillMaxSize(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(targetState) { album ->
                    AlbumCard(
                        album = album,
                        onAlbumClick = onAlbumClick
                    )
                }
            }
        }
    }

    BackHandler(
        enabled = isExpanded
    ) {
        toggleExpand()
    }
}

@Preview
@Composable
fun LibraryScreenTest(
    isExpanded: Boolean = false,
    toggleExpand: () -> Unit = {},
    albumList: List<AlbumUiModel> = listOf(
        AlbumUiModel(title = "에피소드", artist = "이무진", albumId = 10000001, albumArtUri = Uri.EMPTY),
        AlbumUiModel(title = "아파트", artist = "로제", albumId = 10000002, albumArtUri = Uri.EMPTY),
    ),
    onAlbumClick: (AlbumUiModel) -> Unit = {},
) {
    MusicPlayerTheme {
        LibraryScreen(
            isExpanded = isExpanded,
            toggleExpand = toggleExpand,
            albumList = albumList,
            onAlbumClick = onAlbumClick,
        )
    }
}