package com.nexters.misik.domain

interface WebBridgeContract {
    fun openCamera()
    fun openGallery()
    fun share(content: String)
    fun createReview(ocrText: String, hashTags: List<String>, reviewStyle: String)
    fun copy(review: String)
}
