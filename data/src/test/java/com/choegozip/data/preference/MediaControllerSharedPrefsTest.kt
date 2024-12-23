package com.choegozip.data.preference

import com.choegozip.domain.model.ComponentInfo
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import javax.inject.Inject

@HiltAndroidTest
@RunWith(RobolectricTestRunner::class)
@Config(application = HiltTestApplication::class)
class MediaControllerSharedPrefsTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var mediaControllerSharedPrefs: MediaControllerSharedPrefs

    @Before
    fun setup() {
        hiltRule.inject()
    }

    // TODO context 를 이용해서 프리퍼런스에 직접 접근하여 데이터 동일한지 확인하는것으로 변경 및 함수 소분
    @Test
    fun `setUiComponent getUiComponent 결과가 같은지 확인한다`() {
        // Given
        val componentInfo = ComponentInfo("com.example", "MainActivity")

        // When
        mediaControllerSharedPrefs.setUiComponent(componentInfo)

        // Then
        val storedComponent = mediaControllerSharedPrefs.getUiComponent()
        assertEquals(componentInfo, storedComponent)
    }

    // TODO context 를 이용해서 프리퍼런스에 직접 접근하여 데이터 제거되는지 확인하는것으로 변경
    @Test
    fun `clearUiComponent 동작이 잘 되는지 확인한다`() {
        // Given
        val componentInfo = ComponentInfo("com.example", "MainActivity")

        // When
        mediaControllerSharedPrefs.setUiComponent(componentInfo)
        mediaControllerSharedPrefs.clearUiComponent()

        // Then
        val storedComponent = mediaControllerSharedPrefs.getUiComponent()
        assertNull(storedComponent)
    }
}
