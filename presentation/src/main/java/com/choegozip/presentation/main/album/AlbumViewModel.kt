package com.choegozip.presentation.main.album

import androidx.lifecycle.ViewModel
import com.choegozip.domain.model.playback.PlayMedia
import com.choegozip.domain.usecase.media.GetMediaListUseCase
import com.choegozip.domain.usecase.playback.PlayMediaUseCase
import com.choegozip.presentation.model.MediaUiModel
import com.choegozip.presentation.model.toUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class AlbumViewModel @Inject constructor(
    private val getMediaListUseCase: GetMediaListUseCase,
    private val playMediaUseCase: PlayMediaUseCase,
) : ViewModel(), ContainerHost<AlbumState, AlbumSideEffect> {

    override val container: Container<AlbumState, AlbumSideEffect> = container(
        initialState = AlbumState(),
        buildSettings = {
            this.exceptionHandler = CoroutineExceptionHandler { _, throwable ->
                intent { postSideEffect(AlbumSideEffect.Toast(throwable.message.orEmpty())) }
            }
        }
    )

    /**
     * 미디어 리스트 가져오기
     */
    fun getMediaList(albumId: Long) = intent {
        val mediaList = getMediaListUseCase(albumId).getOrThrow().map {
            it.toUiModel()
        }

        reduce {
            state.copy(
                mediaList = mediaList
            )
        }
    }

    /**
     * 미디어 재생하기
     */
    fun playMedia(playMedia: PlayMedia) = intent {
        playMediaUseCase(playMedia).getOrThrow()
    }
}

data class AlbumState(
    // 전체 미디어 리스트
    val mediaList: List<MediaUiModel> = emptyList(),
)

sealed interface AlbumSideEffect {
    class Toast(val message: String) : AlbumSideEffect
}