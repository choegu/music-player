package com.choegozip.data.repository

import android.content.ContentResolver
import android.content.Context
import android.database.MatrixCursor
import android.provider.MediaStore
import androidx.test.platform.app.InstrumentationRegistry
import com.choegozip.domain.model.media.Media
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import javax.inject.Inject

@HiltAndroidTest
class MediaItemRepositoryTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var repository: MediaItemRepository

    @Before
    fun setup() {
        hiltRule.inject()

        // TODO content resolver cursor 에 fake data 만들기
//        MockitoAnnotations.openMocks(this)
//
//        val cursor = MatrixCursor(projection)
//        cursor.addRow(arrayOf(1L, "Song 1", "/data/song1.mp3", "Artist 1", "Album 1", 100L, 300000L, "Title 1"))
//        cursor.addRow(arrayOf(2L, "Song 2", "/data/song2.mp3", "Artist 2", "Album 2", 200L, 200000L, "Title 2"))
//
//        // Mock ContentResolver Query Behavior
//        Mockito.`when`(
//            mockContentResolver.query(
//                Mockito.eq(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI),
//                Mockito.any(),
//                Mockito.any(),
//                Mockito.any(),
//                Mockito.any()
//            )
//        ).thenReturn(cursor)
//
//        // Mock Cursor Behavior
//        Mockito.`when`(cursor.moveToFirst()).thenReturn(true)
//        Mockito.`when`(cursor.getString(Mockito.anyInt())).thenReturn("Test Title")
//        Mockito.`when`(cursor.getLong(Mockito.anyInt())).thenReturn(123L)
    }

//    private val mockContext = Mockito.mock(Context::class.java)
//    private val mockContext = InstrumentationRegistry.getInstrumentation().targetContext
    private val mockContentResolver = Mockito.mock(ContentResolver::class.java)

//    private val repository = MediaItemRepository(mockContext)
    private val projection = arrayOf(
        MediaStore.Audio.Media._ID,
        MediaStore.Audio.Media.DISPLAY_NAME,
        MediaStore.Audio.Media.DATA,
        MediaStore.Audio.Media.ARTIST,
        MediaStore.Audio.Media.ALBUM,
        MediaStore.Audio.Media.ALBUM_ID,
        MediaStore.Audio.Media.DURATION,
        MediaStore.Audio.Media.TITLE
    )
    private val selection = "${MediaStore.Audio.Media.IS_MUSIC} != 0"
    private val sortOrder = MediaStore.Audio.Media.TITLE + " ASC"

    // TODO before 에서 만들어진 fake 데이터로 체크할 수 있게 수정
    @Test
    fun getAllMusicAsMediaItems_should_fetch_and_map_data_correctly() {
        // Arrange
//        val cursor = MatrixCursor(projection)
//        cursor.addRow(arrayOf(1L, "Song 1", "/data/song1.mp3", "Artist 1", "Album 1", 100L, 300000L, "Title 1"))
//        cursor.addRow(arrayOf(2L, "Song 2", "/data/song2.mp3", "Artist 2", "Album 2", 200L, 200000L, "Title 2"))
//
//        Mockito.`when`(mockContext.contentResolver).thenReturn(mockContentResolver)
//        Mockito.`when`(
//            mockContentResolver.query(
//                Mockito.eq(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI),
//                projection,
//                selection,
//                null,
//                sortOrder
//            )
//        ).thenReturn(cursor)


        // Act
        val mediaItems = repository.getAllMusicAsMediaItems()

        // Assert
        assertEquals(2, mediaItems.size)
        assertEquals("Song 1", mediaItems[0].displayName)
        assertEquals("Artist 2", mediaItems[1].artist)
    }

    @Test
    fun createAlbumList_should_create_unique_album_list() {
        // Arrange
        val mediaItems = listOf(
            Media(1, "Song 1", "Artist 1", "Album 1", 100, 300000L, "Title 1"),
            Media(2, "Song 2", "Artist 1", "Album 1", 100, 300000L, "Title 2"),
            Media(3, "Song 3", "Artist 2", "Album 2", 200, 400000L, "Title 3")
        )

        // Act
        val albums = repository.createAlbumList(mediaItems)

        // Assert
        assertEquals(2, albums.size)
        assertEquals("Album 1", albums[0].title)
        assertEquals("Artist 2", albums[1].artist)
    }

    // TODO before 에서 만들어진 fake 데이터로 체크할 수 있게 수정
    @Test
    fun getMediaListByAlbum_should_return_filtered_media_items() {
        // Arrange
//        val mediaItems = listOf(
//            Media(1, "Song 1", "Artist 1", "Album 1", 100, 300000L, "Title 1"),
//            Media(2, "Song 2", "Artist 1", "Album 1", 100, 300000L, "Title 2"),
//            Media(3, "Song 3", "Artist 2", "Album 2", 200, 400000L, "Title 3")
//        )
//        repository.createAlbumList(mediaItems)
//
//        // Act
//        val filteredMedia = repository.getMediaListByAlbum(100)
//
//        // Assert
//        assertEquals(2, filteredMedia.size)
//        assertTrue(filteredMedia.all { it.albumId == 100L })
    }
}
