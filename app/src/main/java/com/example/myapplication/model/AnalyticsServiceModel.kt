package com.example.myapplication.model

class AnalyticsServiceModel {

    enum class AnalysisStatus {
        NO,
        IN_PROGRESS,
        COMPLETE,
        USER_NOT_FOUND,
        ERROR
    }

    data class UploadPictureResult(val likes: Int, val status: AnalysisStatus)
}