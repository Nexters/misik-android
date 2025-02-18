package com.nexters.misik.core.ui

import androidx.activity.ComponentActivity

interface PreviewServiceContract {
    fun init(activity: ComponentActivity)
    fun openCamera(callback: (String?) -> Unit)
    fun openGallery(callback: (String?) -> Unit)
}
