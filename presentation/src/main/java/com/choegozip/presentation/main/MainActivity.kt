package com.choegozip.presentation.main

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import com.choegozip.presentation.theme.MusicPlayerTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlin.system.exitProcess

/**
 * 음악 앱 특성 상, 권한 없이 진행할 수 있는 동작이 거의 없기 때문에, 액티비티 시점에 권한 체크하여,
 * 백그라운드에서 권한 거부 후, 액티비티가 재생성 되는 시나리오가 되더라도, 권한체크할 수 있게 구성.
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    // 스크린 시작 여부 플래그
    private var isStartScreen = false

    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        setContent {
            MusicPlayerTheme {
                MainNavHost()
            }
        }



//        checkPermissionAndProceed()
    }

    /**
     * 권한 확인
     */
    private fun checkPermissionAndProceed() {
        val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.READ_MEDIA_AUDIO
        } else {
            Manifest.permission.READ_EXTERNAL_STORAGE
        }

        if (ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED) {
            startScreen()
        } else {
            requestPermissionLauncher.launch(permission)
        }
    }

    /**
     * 권한 요청 런처
     */
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            startScreen()
        } else {
            if (!permissionDeniedDialog.isShowing) permissionDeniedDialog.show()
        }
    }

    /**
     * 권한 거부되었을 시, 요청 다이얼로그
     */
    private val permissionDeniedDialog by lazy {
        AlertDialog.Builder(this)
            .setTitle("권한 필요")
            .setMessage("앱을 사용하려면 권한이 필요합니다.")
            .setPositiveButton("권한 설정으로 이동") { _, _ ->
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                    data = Uri.fromParts("package", packageName, null)
                }
                startActivity(intent)
            }
            .setNegativeButton("앱 종료") { _, _ ->
                finishAffinity()
                exitProcess(0)
            }
            .setCancelable(false)
            .create()
    }

    /**
     * 화면 시작
     */
    private fun startScreen() {
        if (permissionDeniedDialog.isShowing) permissionDeniedDialog.dismiss()

        if (!isStartScreen) {
            setContent {
                MusicPlayerTheme {
                    MainNavHost()
                }
            }
        }
        isStartScreen = true
    }
}