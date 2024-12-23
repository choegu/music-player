package com.choegozip.domain.model.playback

data class PlayMedia (
    val albumId: Long,
    val mediaIndex: Int?,
    val shuffleModeEnabled: Boolean,
)