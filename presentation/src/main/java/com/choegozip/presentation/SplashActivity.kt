package com.choegozip.presentation

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.lifecycle.lifecycleScope
import com.choegozip.presentation.main.MainActivity
import com.gun0912.tedpermission.coroutine.TedPermission
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO 스크린

        lifecycleScope.launch {
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

            delay(500)

            // TODO 채널 생성

            Log.d("!!!!!", "result ${permissionResult.isGranted}")


            if (permissionResult.isGranted) {
                finish()
                startActivity(Intent(this@SplashActivity, MainActivity::class.java))
            } else {
                // TODO 안내 다이얼로그 띄우기
            }
        }
    }
}