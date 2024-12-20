package com.choegozip.domain.model

data class Media(
    val id:Long,
    val displayName:String,
    val data:String,
    val artist:String,
    val albumTitle:String,
    val albumId:Long,
    val duration:Long,
    val title:String,
)