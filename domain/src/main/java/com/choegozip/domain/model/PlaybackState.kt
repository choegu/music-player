package com.choegozip.domain.model

data class PlaybackState (
    val duration: Long,
    val currentPosition: Long,
) {
    companion object {
        fun empty() = PlaybackState(0, 0)
    }
}