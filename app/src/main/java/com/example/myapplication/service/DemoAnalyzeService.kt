package com.example.myapplication.service

import android.net.Uri
import com.example.myapplication.model.AnalyticsServiceModel
import com.example.myapplication.model.AnalyticsServiceModel.AnalysisStatus.*
import com.example.myapplication.model.AnalyticsServiceModel.UploadPictureResult

import java.util.*

class DemoAnalyzeService : AnalyzeService {

    var statusCheckStatuses = listOf(IN_PROGRESS, COMPLETE)

    override fun analyse(userProfile: String): AnalyticsServiceModel.AnalysisStatus {
        val random = Random().nextInt() % 2 == 0
        return if (random) USER_NOT_FOUND else COMPLETE
    }

    override fun checkStatus(userProfile: String): AnalyticsServiceModel.AnalysisStatus {
        return statusCheckStatuses[Random().nextInt(statusCheckStatuses.size)]
    }

    override fun uploadPicture(userProfile: String, imageUri: Uri?): UploadPictureResult {
        return UploadPictureResult(Random().nextInt(256), COMPLETE)
    }
}
