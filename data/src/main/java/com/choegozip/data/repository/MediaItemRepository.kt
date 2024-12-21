package com.choegozip.data.repository

import android.content.Context
import android.provider.MediaStore
import com.choegozip.domain.model.Album
import com.choegozip.domain.model.Media
import javax.inject.Inject
import javax.inject.Singleton

/**
 * 미디어 데이터 저장소
 */
@Singleton
class MediaItemRepository @Inject constructor(
    private val context: Context
) {

    // TODO 클래스 초기화 대비하여 룸에 구성
    // TODO 변경되는 미디어는 브로드캐스트 통해서 룸에 업데이트
    private lateinit var totalAlbumList: List<Album>
    private lateinit var totalMediaList: List<Media>

    /**
     * 모든 음악 형태의 미디어 아이템 가져오기
     */
    fun getAllMusicAsMediaItems(): List<Media> {
        val mediaItems = mutableListOf<Media>()

        val projection = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.DISPLAY_NAME,
            MediaStore.Audio.Media.DATA,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.ALBUM,
            MediaStore.Audio.Media.ALBUM_ID,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.TITLE
        )

        val selection = "${MediaStore.Audio.Media.IS_MUSIC} != 0"

        val cursor = context.contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            projection,
            selection,
            null,
            MediaStore.Audio.Media.TITLE + " ASC"
        )

        cursor?.use {
            val idColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
            val displayNameColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME)
            val dataColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)
            val artistColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)
            val albumColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM)
            val albumIdColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID)
            val durationColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)
            val titleColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)

            while (it.moveToNext()) {
                val id = it.getLong(idColumn)
                val displayName = it.getString(displayNameColumn)
                val data = it.getString(dataColumn)
                val artist = it.getString(artistColumn)
                val albumTitle = it.getString(albumColumn)
                val albumId = it.getLong(albumIdColumn)
                val duration = it.getLong(durationColumn)
                val title = it.getString(titleColumn)

                val mediaItem = Media(
                    id = id,
                    displayName = displayName,
                    data = data,
                    artist = artist,
                    albumTitle = albumTitle,
                    albumId = albumId,
                    duration = duration,
                    title = title,
                )

                mediaItems.add(mediaItem)
            }
        }

        totalMediaList = mediaItems

        return mediaItems
    }

    /**
     * 앨범리스트 생성 후 가져오기
     */
    fun createAlbumList(mediaItemList: List<Media>): List<Album> {
        val albumList = mutableListOf<Album>()

        mediaItemList.forEach { mediaItem ->
            if (albumList.none { it.albumId == mediaItem.albumId }) {
                albumList.add(
                    Album(
                        title = mediaItem.albumTitle,
                        artist = mediaItem.artist,
                        albumId = mediaItem.albumId,
                    )
                )
            }
        }

        totalAlbumList = albumList

        return albumList
    }

    /**
     * 미디어 리스트 가져오기
     */
    fun getMediaList(albumId: Long): List<Media> {
        val mediaList = totalMediaList.filter {
            it.albumId == albumId
        }
        return mediaList
    }
}
