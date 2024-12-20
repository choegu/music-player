package com.choegozip.presentation.main.library

import android.widget.Toast
import androidx.annotation.OptIn
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.media3.common.util.UnstableApi
import com.choegozip.presentation.main.MainSideEffect
import com.choegozip.presentation.main.MainViewModel
import com.choegozip.presentation.model.AlbumUiModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@OptIn(UnstableApi::class)
@Composable
fun LibraryScreen(
    mainViewModel: MainViewModel,
    onNavigateToAlbumScreen: () -> Unit,
) {
    val state = mainViewModel.collectAsState().value
    val context = LocalContext.current

    // 예외 발생 시, 토스트로 처리
    mainViewModel.collectSideEffect { sideEffect->
        when(sideEffect){
            is MainSideEffect.Toast -> Toast.makeText(context, sideEffect.message, Toast.LENGTH_SHORT).show()
            MainSideEffect.NavigateToAlbumScreen -> onNavigateToAlbumScreen()
        }
    }

    LibraryScreen(
        albumList = state.albumList,
        onAlbumClick = mainViewModel::onSelectAlbum
    )
}

@Composable
private fun LibraryScreen(
    albumList: List<AlbumUiModel>,
    onAlbumClick: (AlbumUiModel) -> Unit
) {
    Surface {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(albumList) { album ->
                AlbumCard(
                    album = album,
                    onAlbumClick = onAlbumClick
                )
            }
        }
    }
}

// TODO preview