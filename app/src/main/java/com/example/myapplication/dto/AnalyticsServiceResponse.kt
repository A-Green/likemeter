package com.example.myapplication.dto

class AnalyticsServiceResponse {

    data class UploadPictureResponse(val likes: Int)

    data class StatusCheckResponse(val status: String)
}