package com.nexters.misik.preview.util

import android.content.Context
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts

class PermissionHandler {
    private lateinit var context: Context
    private lateinit var permissionLauncher: ActivityResultLauncher<String>

    fun init(activity: ComponentActivity) {
        this.context = activity

        // 퍼미션 요청 런처 초기화
        this.permissionLauncher = activity.registerForActivityResult(
            ActivityResultContracts.RequestPermission(),
        ) { isGranted ->
            if (!isGranted) {
                Toast.makeText(context, "권한이 필요합니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun requestPermission(permission: String, onGranted: () -> Unit) {
        if (context.checkSelfPermission(permission) == android.content.pm.PackageManager.PERMISSION_GRANTED) {
            onGranted()
        } else {
            permissionLauncher.launch(permission)
        }
    }
}
