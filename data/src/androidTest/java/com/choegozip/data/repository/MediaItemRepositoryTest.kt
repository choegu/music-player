package com.choegozip.data.repository

import android.content.ContentResolver
import android.content.Context
import android.database.MatrixCursor
import android.provider.MediaStore
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.GrantPermissionRule
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

    @get:Rule
    val permissionRule: GrantPermissionRule = GrantPermissionRule.grant(
        android.Manifest.permission.READ_EXTERNAL_STORAGE,
        android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
        android.Manifest.permission.READ_MEDIA_AUDIO,
    )

    @Inject
    lateinit var repository: MediaItemRepository

    @Before
    fun setup() {
        hiltRule.inject()
    }

    // TODO 모킹 데이터로 체크할 수 있게 수정
    @Test
    fun getAllMusicAsMediaItems_should_fetch_and_map_data_correctly() {
        // Given

        // When
        // 실제 디바이스 데이터
        val mediaItems = repository.getAllMusicAsMediaItems()

        // Then
        assertNotEquals(0, mediaItems.size)
        assertEquals(22, mediaItems.size)
        assertNotNull(mediaItems.firstOrNull { it.artist == "The Weeknd" })
    }

    @Test
    fun createAlbumList_should_create_unique_album_list() {
        // Given
        val mediaItems = listOf(
            Media(1, "Song 1", "Artist 1", "Album 1", 100, 300000L, "Title 1"),
            Media(2, "Song 2", "Artist 1", "Album 1", 100, 300000L, "Title 2"),
            Media(3, "Song 3", "Artist 2", "Album 2", 200, 400000L, "Title 3")
        )

        // When
        val albums = repository.createAlbumList(mediaItems)

        // Then
        assertEquals(2, albums.size)
        assertEquals("Album 1", albums[0].title)
        assertEquals("Artist 2", albums[1].artist)
    }

    // TODO fake 데이터로 체크할 수 있게 수정
    @Test
    fun getMediaListByAlbum_should_return_filtered_media_items() {
        // Given
        // 실제 디바이스 데이터
        repository.getAllMusicAsMediaItems()

        // When
        // 앨범 : Starboy (Explicit Ver.)
        val filteredMedia = repository.getMediaListByAlbum(6658609919884024256)

        // Then
        assertTrue(filteredMedia.all { it.albumId == 6658609919884024256 })
    }
}
