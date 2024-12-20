package com.choegozip.presentation.model

import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import com.choegozip.domain.model.Media

data class MediaUiModel (
    val mediaList: MediaItem,
    val isSelected: Boolean,
)

fun Media.toUiModel(): MediaUiModel {
    // 음악 파일의 URI
    val contentUri = Uri.withAppendedPath(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id.toString())
    // 앨범 아트 URI
    val albumArtUri = Uri.parse("content://media/external/audio/albumart/$albumId")
    // MediaItem 생성
    val mediaItem = MediaItem.Builder()
        .setMediaId(id.toString())
        .setUri(contentUri)
        .setMediaMetadata(
            MediaMetadata.Builder()
                .setTitle(title)
                .setArtist(artist)
                .setAlbumTitle(albumTitle)
                .setArtworkUri(albumArtUri)
                .setDescription(displayName)
                .setExtras(
                    Bundle().apply {
                        putString("sourceUri", data)
                        putLong("duration", duration)
                    }
                )
                .build()
        )
        .build()

    return MediaUiModel(
        isSelected = false,
        mediaList = mediaItem
    )
}