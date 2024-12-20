package com.choegozip.presentation.model

import android.net.Uri
import com.choegozip.domain.model.Album

data class AlbumUiModel (
    val title: String,
    val artist: String,
    val albumArtUri: Uri,
    val mediaList: List<MediaUiModel>,
    val isSelected: Boolean,
)

fun Album.toUiModel(): AlbumUiModel {
    return AlbumUiModel(
        title = title,
        artist = artist,
        albumArtUri = Uri.parse("content://media/external/audio/albumart/$albumId"),
        mediaList = mediaList.map { it.toUiModel() },
        isSelected = false
    )
}