package com.choegozip.data.repository

import android.content.Context
import androidx.media3.common.C
import androidx.test.rule.GrantPermissionRule
import app.cash.turbine.test
import com.choegozip.data.service.PlaybackService
import com.choegozip.domain.model.ComponentInfo
import com.choegozip.domain.model.media.Media
import com.choegozip.domain.model.playback.PlayMedia
import com.choegozip.domain.model.playback.PlaybackPosition
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
class PlaybackRepositoryTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @get:Rule
    val permissionRule: GrantPermissionRule = GrantPermissionRule.grant(
        android.Manifest.permission.READ_EXTERNAL_STORAGE,
        android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
        android.Manifest.permission.READ_MEDIA_AUDIO,
        android.Manifest.permission.FOREGROUND_SERVICE,
    )

    @Inject
    lateinit var context: Context

    @Inject
    lateinit var playbackRepository: PlaybackRepository

    @Inject
    lateinit var mediaItemRepository: MediaItemRepository

    private lateinit var uiComponentInfo: ComponentInfo

    @Before
    fun setUp() {
        hiltRule.inject()

        uiComponentInfo = ComponentInfo(
            packageName = context.packageName,
            className = "com.choegozip.presentation.main.MainActivity"
        )
    }

    @Test
    fun getPlaybackComponent_return_canonicalName() = runTest {
        // Given

        // When
        val playbackComponentInfo = playbackRepository.getPlaybackComponent(uiComponentInfo)

        // Then
        assertNotNull(playbackComponentInfo)
        assertEquals(
            playbackComponentInfo.packageName,
            context.packageName,
        )
        assertEquals(
            playbackComponentInfo.className,
            PlaybackService::class.java.canonicalName,
        )
    }

    /**
     * 기기에 2곡 이상 있는 경우 테스트 가능
     */
    @Test
    fun playMedia_emit_correctState() = runTest {
        // Given
        val playWhenReadyFlow = MutableSharedFlow<Boolean>(replay = 1)
        val mediaItemTransitionFlow = MutableSharedFlow<Media>(replay = 1)
        val deviceMediaList = mediaItemRepository.getAllMusicAsMediaItems()
        val playMedia = PlayMedia(
            albumId = deviceMediaList[0].albumId,
            mediaIndex = null,
            shuffleModeEnabled = false
        )

        // When
        playbackRepository.getPlaybackComponent(uiComponentInfo)
        playbackRepository.getPlayWhenReady(playWhenReadyFlow)
        playbackRepository.getMediaItemTransition(mediaItemTransitionFlow)
        playbackRepository.playMedia(playMedia, deviceMediaList)

        // Then
        playWhenReadyFlow.test {
            assertEquals(true, awaitItem())
            playbackRepository.playOrPauseMedia()
            assertEquals(false, awaitItem())
            playbackRepository.playOrPauseMedia()
            assertEquals(true, awaitItem())
        }
        mediaItemTransitionFlow.test {
            val awaitItem = awaitItem()
            assertEquals(deviceMediaList.firstOrNull()?.id, awaitItem.id)
            assertEquals(deviceMediaList.firstOrNull()?.title, awaitItem.title)
        }
    }

    @Test
    fun getPlayWhenReady_should_emit_playWhenReady_states() = runTest{
        // Given
        val flow = MutableSharedFlow<Boolean>(replay = 1)
        playbackRepository.getPlaybackComponent(uiComponentInfo)

        // When
        playbackRepository.getPlayWhenReady(flow)

        // Then
        flow.test {
            assertEquals(false, awaitItem()) // 기본 상태 확인
            playbackRepository.playOrPauseMedia()
            assertEquals(true, awaitItem()) // 상태 변경 확인
        }
    }

    @Test
    fun getPositionChanged_should_emit_PlaybackPosition_states() = runTest {
        // Given
        val flow = MutableSharedFlow<PlaybackPosition>(replay = 1)
        playbackRepository.getPlaybackComponent(uiComponentInfo)

        // When
        playbackRepository.getPositionChanged(flow)

        // Then
        flow.test {
            val initialPosition = awaitItem()
            assertEquals(0L, initialPosition.currentPosition)
            assertEquals(C.TIME_UNSET, initialPosition.duration)
        }
    }
}
