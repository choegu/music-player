package com.choegozip.data.repository

import android.content.Context
import android.provider.MediaStore
import com.choegozip.domain.model.media.Album
import com.choegozip.domain.model.media.Media
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
     * 미디어스토어에서 음악 아이템 가져오기
     *
     * TODO contentResolver query 리턴값을 받아서 처리하도록 함수를 나누면, 테스트 케이스 추가 생성 가능
     */
    fun getAllMusicAsMediaItems(): List<Media> {
        val mediaItems = mutableListOf<Media>()

        // 가져올 컬럼명 지정
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

        // 음악 파일을 가져오기 위한 조건
        val selection = "${MediaStore.Audio.Media.IS_MUSIC} != 0"

        // 미디어스토어에 쿼리 실행
        val cursor = context.contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            projection,
            selection,
            null,
            MediaStore.Audio.Media.TITLE + " ASC"
        )

        // 커서 사용 후 닫기
        cursor?.use {

            // 각 컬럼명에 해당하는 인덱스 가져오기
            val idColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
            val displayNameColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME)
            val dataColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)
            val artistColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)
            val albumColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM)
            val albumIdColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID)
            val durationColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)
            val titleColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)

            // 커서 행 반복
            while (it.moveToNext()) {

                // 인덱스를 이용해 각 행의 데이터 읽어오기
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
    fun getMediaListByAlbum(albumId: Long): List<Media> {
        val mediaList = totalMediaList.filter {
            it.albumId == albumId
        }
        return mediaList
    }
}
