package com.choegozip.presentation.model

import android.net.Uri
import com.choegozip.domain.model.Media

data class MediaUiModel (
    val id: Long,
    val albumTitle: String,
    val artist: String,
    val mediaTitle: String,
    val albumArtUri: Uri,
) {
    companion object {
        fun empty(): MediaUiModel {
            return MediaUiModel(
                id = -1,
                albumTitle = "",
                artist = "",
                mediaTitle = "",
                albumArtUri = Uri.EMPTY
            )
        }
    }

    /**
     * 비어있는 미디어인지 확인
     */
    fun isEmpty() = id < 0
}

fun Media.toUiModel(): MediaUiModel {
    return MediaUiModel(
        id = id,
        albumTitle = albumTitle,
        artist = artist,
        mediaTitle = title,
        albumArtUri = Uri.parse("content://media/external/audio/albumart/$albumId")
    )
}