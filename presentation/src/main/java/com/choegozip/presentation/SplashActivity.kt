package com.choegozip.presentation

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.OptIn
import androidx.appcompat.app.AlertDialog
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.media3.common.util.UnstableApi
import com.choegozip.presentation.main.MainActivity
import com.choegozip.presentation.theme.MusicPlayerTheme
import com.gun0912.tedpermission.coroutine.TedPermission
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity: ComponentActivity() {
    @OptIn(UnstableApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 스플래시 화면 구현
        setContent {
            MusicPlayerTheme {
                Column (
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    Spacer(modifier = Modifier.height(64.dp))
                    Text(
                        text = "MUSIC PLAYER",
                        fontSize = 28.sp,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally),
                    )
                }
            }
        }

        // STARTED 시점 마다 권한 체크 (권한 설정 페이지 다녀오는 경우 대응)
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                // 권한 리스트
                val permissionList = mutableListOf<String>().apply {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        add(Manifest.permission.READ_MEDIA_AUDIO)
                        add(Manifest.permission.POST_NOTIFICATIONS)
                    } else {
                        add(Manifest.permission.READ_EXTERNAL_STORAGE)
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                        add(Manifest.permission.FOREGROUND_SERVICE_MEDIA_PLAYBACK)
                    }
                }

                val permissionResult = TedPermission.create()
                    .setPermissions(*permissionList.toTypedArray())
                    .check()

                delay(1000)

                if (permissionResult.isGranted) {
                    finish()
                    startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                } else {
                    showPermissionDialog()
                }

            }
        }

    }

    /**
     * 권한 요청 다이얼로그 보이기
     */
    private fun showPermissionDialog() {
        val dialog = AlertDialog.Builder(this)
            .setTitle("권한 필요")
            .setMessage("권한을 허용해주세요.")
            .setPositiveButton("앱 종료") { _, _ ->
                finishAffinity() // 앱 종료
            }
            .setNegativeButton("권한 설정하기") { _, _ ->
                // 권한 설정 화면으로 이동
                moveToPermissionSettings()
            }
            .setCancelable(false) // 다이얼로그 바깥 영역 클릭 시 닫히지 않도록 설정
            .create()

        dialog.show()
    }

    /**
     * 권한 설정 페이지로 이동
     */
    private fun moveToPermissionSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.fromParts("package", packageName, null)
        }
        startActivity(intent)
    }
}