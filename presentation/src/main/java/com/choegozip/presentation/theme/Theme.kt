package com.choegozip.presentation.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

// Light 테마 컬러 스킴
private val LightColorScheme = lightColorScheme(
    primary = Blue,              // 주요 색상: TopAppBar, 버튼 배경
    onPrimary = White,           // primary 위의 텍스트 색상
    secondary = LightBlue,       // 보조 색상: 인터랙션 버튼
    onSecondary = Black,         // secondary 위의 텍스트 색상
    background = White,          // 전체 배경 색상
    onBackground = Black,        // 배경 위의 텍스트 색상
    surface = White,             // 카드, 리스트 항목 표면 색상
    onSurface = Black,           // surface 위의 텍스트 색상
    outline = Gray               // 구분선, 테두리 색상
)

// 테마 적용 함수
@Composable
fun MusicPlayerTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography,
        content = content
    )
}