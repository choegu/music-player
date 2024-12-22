package com.choegozip.presentation.model

import android.net.Uri
import com.choegozip.domain.model.Media

data class MediaUiModel (
    val albumTitle: String,
    val artist: String,
    val mediaTitle: String,
    val albumArtUri: Uri,
) {
    companion object {
        fun empty(): MediaUiModel {
            return MediaUiModel(
                albumTitle = "",
                artist = "",
                mediaTitle = "",
                albumArtUri = Uri.EMPTY
            )
        }
    }
}

fun Media.toUiModel(): MediaUiModel {
    return MediaUiModel(
        albumTitle = albumTitle,
        artist = artist,
        mediaTitle = title,
        albumArtUri = Uri.parse("content://media/external/audio/albumart/$albumId")
    )
}