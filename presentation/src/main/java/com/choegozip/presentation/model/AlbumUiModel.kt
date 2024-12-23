package com.choegozip.presentation.model

import android.net.Uri
import com.choegozip.domain.model.media.Album

data class AlbumUiModel (
    val title: String,
    val artist: String,
    val albumId: Long,
    val albumArtUri: Uri,
)

fun Album.toUiModel(): AlbumUiModel {
    return AlbumUiModel(
        title = title,
        artist = artist,
        albumId = albumId,
        albumArtUri = Uri.parse("content://media/external/audio/albumart/$albumId"),
    )
}