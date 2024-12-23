package com.choegozip.presentation.library

import android.net.Uri
import androidx.activity.ComponentActivity
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTouchInput
import androidx.test.espresso.Espresso.pressBack
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.choegozip.presentation.main.library.LibraryScreenTest
import com.choegozip.presentation.model.AlbumUiModel
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LibraryScreenTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private val mockAlbumUiModels = listOf(
        AlbumUiModel(title = "에피소드", artist = "이무진", albumId = 10000001, albumArtUri = Uri.EMPTY),
        AlbumUiModel(title = "아파트", artist = "로제", albumId = 10000002, albumArtUri = Uri.EMPTY),
    )

    @Before
    fun setup() {

    }

    @Test
    fun albumList_DisplaysCorrectly() {
        // Given

        // When
        composeTestRule.setContent {
            LibraryScreenTest(
                isExpanded = false,
                toggleExpand = {},
                albumList = mockAlbumUiModels,
                onAlbumClick = {}
            )
        }

        // Then
        composeTestRule.onNodeWithText("에피소드").assertExists()
        composeTestRule.onNodeWithText("이무진").assertExists()
        composeTestRule.onNodeWithText("아파트").assertExists()
    }

    @Test
    fun clickAlbum_NavigatesToAlbumScreen() {
        // Given
        var navigationCalled = false

        // When
        composeTestRule.setContent {
            LibraryScreenTest(
                isExpanded = false,
                toggleExpand = {},
                albumList = mockAlbumUiModels,
                onAlbumClick = { navigationCalled = true }
            )
        }

        // Then
        composeTestRule.onNodeWithText("로제").performClick()
        assertTrue(navigationCalled)
    }

    @Test
    fun backPress_WhenExpanded_CallsToggleExpand() {
        // Given
        var toggleExpandCalled = false

        // When
        composeTestRule.setContent {
            LibraryScreenTest(
                isExpanded = true,
                toggleExpand = { toggleExpandCalled = true },
                albumList = emptyList(),
                onAlbumClick = {}
            )
        }

        // Then
        composeTestRule.onRoot().performTouchInput { pressBack() }
        assertTrue(toggleExpandCalled)
    }
}