package com.choegozip.presentation.main

import androidx.lifecycle.ViewModel
import com.choegozip.domain.model.Media
import com.choegozip.domain.model.ComponentInfo
import com.choegozip.domain.model.PlaybackState
import com.choegozip.domain.usecase.GetAlbumListUseCase
import com.choegozip.domain.usecase.GetPlaybackComponentUseCase
import com.choegozip.domain.usecase.GetPlaybackStateUseCase
import com.choegozip.domain.usecase.PlayMediaUseCase
import com.choegozip.presentation.model.AlbumUiModel
import com.choegozip.presentation.model.toUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.delay
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getAlbumListUseCase: GetAlbumListUseCase,
    private val getPlaybackComponentUseCase: GetPlaybackComponentUseCase,
    private val playMediaUseCase: PlayMediaUseCase,
    private val getPlaybackStateUseCase: GetPlaybackStateUseCase,
) : ViewModel(), ContainerHost<MainState, MainSideEffect> {

    override val container: Container<MainState, MainSideEffect> = container(
        initialState = MainState(),
        buildSettings = {
            this.exceptionHandler = CoroutineExceptionHandler { _, throwable ->
                intent { postSideEffect(MainSideEffect.Toast(throwable.message.orEmpty())) }
            }
        }
    )

    init {
        getAlbumList()
    }

    /**
     * 앨범 리스트 가져오기
     */
    private fun getAlbumList() = intent {
        val albumList = getAlbumListUseCase().getOrThrow().map {
            it.toUiModel()
        }

        reduce {
            state.copy(
                albumList = albumList
            )
        }
    }

    /**
     * 재생 컴포넌트 정보 가져오기
     */
    fun getPlaybackComponent(uiComponentInfo: ComponentInfo) = intent {
        val playbackComponentInfo = getPlaybackComponentUseCase(uiComponentInfo).getOrThrow()

        reduce {
            state.copy(
                playbackComponentInfo = playbackComponentInfo
            )
        }
    }

    /**
     * 앨범 선택
     */
    fun onSelectAlbum(album: AlbumUiModel) = intent {
        reduce {
            state.copy(
                selectedAlbum = album
            )
        }

        postSideEffect(MainSideEffect.NavigateToAlbumScreen)
    }

    /**
     * 미디어 재생
     */
    fun playMedia(album: AlbumUiModel) = intent {
        val mediaList = album.mediaList.map {
            it.mediaItem
            Media(
                id = it.mediaItem.mediaId.toLong(),
                displayName = it.mediaItem.mediaMetadata.description.toString(),
                data = "",
                artist = it.mediaItem.mediaMetadata.artist.toString(),
                albumTitle = it.mediaItem.mediaMetadata.albumTitle.toString(),
                albumId = 1,
                duration = 1,
                title = it.mediaItem.mediaMetadata.title.toString()
            )
        }

        playMediaUseCase(mediaList).getOrThrow()
    }

    /**
     * 미디어 상태 수집 시작
     */
    fun startGetPlaybackState() = intent {
        while (true) {
            // 반복적으로 수집
            delay(2000)

            val playbackState = getPlaybackStateUseCase().getOrThrow()

            reduce {
                state.copy(
                    playbackState = playbackState
                )
            }
        }
    }

    /**
     * TODO 뷰모델 따로 빼기
     * 볼륨 클릭
     */
    fun onClickVolume() = intent {
        postSideEffect(MainSideEffect.ShowVolumeControl)
    }
}

data class MainState(
    // 전체 앨범 리스트
    val albumList: List<AlbumUiModel> = emptyList(),
    // 앨범 스크린 이동을 위한 선택한 앨범
    val selectedAlbum: AlbumUiModel? = null,
    // 디바이스 재생 컴포넌트 정보
    val playbackComponentInfo: ComponentInfo = ComponentInfo.empty(),
    // 재생 상태
    val playbackState: PlaybackState = PlaybackState.empty(),
)

sealed interface MainSideEffect {
    class Toast(val message: String) : MainSideEffect
    data object ShowVolumeControl : MainSideEffect
    data object NavigateToAlbumScreen : MainSideEffect
}