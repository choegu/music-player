package com.choegozip.presentation.main.album

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.choegozip.presentation.model.MediaUiModel

@Composable
fun MediaItemRow(
    media: MediaUiModel,
    index: Int,
    onMediaItemClick: (MediaUiModel) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onMediaItemClick(media) }
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier
                .padding(8.dp),
            text = "${index + 1}",
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.outline
        )

        Text(
            text = media.mediaItem.mediaMetadata.title.toString(),
            fontSize = 16.sp,
            modifier = Modifier.weight(1f)
        )

        IconButton(onClick = { /* 메뉴 클릭 이벤트 */ }) {
            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = "Media Item More Options",
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}