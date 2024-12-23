package com.choegozip.data.model

import androidx.media3.common.MediaItem
import com.choegozip.domain.model.media.Media

fun MediaItem?.toDomainModel(): Media {
    val metadata = this?.mediaMetadata
    val extras = metadata?.extras
    val media = Media(
        id = this?.mediaId?.toLongOrNull() ?: -1,
        displayName = metadata?.description.toString(),
        artist = metadata?.artist.toString(),
        albumTitle = metadata?.albumTitle.toString(),
        albumId = extras?.getLong("albumId") ?: -1,
        duration = extras?.getLong("duration") ?: -1,
        title = metadata?.title.toString()
    )
    return media
}