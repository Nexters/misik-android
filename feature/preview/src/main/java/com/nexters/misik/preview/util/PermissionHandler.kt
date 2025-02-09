package com.nexters.misik.preview.util

import android.app.Activity
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts

class PermissionHandler {
    private lateinit var activity: Activity
    private lateinit var permissionLauncher: ActivityResultLauncher<String>

    fun init(activity: ComponentActivity) {
        this.activity = activity

        // 퍼미션 요청 런처 초기화
        this.permissionLauncher = activity.registerForActivityResult(
            ActivityResultContracts.RequestPermission(),
        ) { isGranted ->
            if (!isGranted) {
                Toast.makeText(activity, "권한이 필요합니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun requestPermission(permission: String, onGranted: () -> Unit) {
        if (activity.checkSelfPermission(permission) == android.content.pm.PackageManager.PERMISSION_GRANTED) {
            onGranted()
        } else {
            permissionLauncher.launch(permission)
        }
    }
}
