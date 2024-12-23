package com.choegozip.domain.model.playback

data class PlaybackPosition (
    val duration: Long,
    val currentPosition: Long,
) {
    companion object {
        fun empty() = PlaybackPosition(0, 0)
    }

    /**
     * 프로그레스 계산
     */
    fun getProgress(): Float {
        return if (duration <= 0) {
            0f
        } else {
            currentPosition.toFloat() / duration.toFloat()
        }
    }
}