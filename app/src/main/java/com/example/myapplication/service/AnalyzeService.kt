package com.example.myapplication.service

import android.net.Uri
import com.example.myapplication.model.AnalyticsServiceModel.AnalysisStatus
import com.example.myapplication.model.AnalyticsServiceModel.UploadPictureResult

interface AnalyzeService {

    fun analyse(userProfile: String): AnalysisStatus

    fun checkStatus(userProfile: String): AnalysisStatus

    fun uploadPicture(userProfile: String, imageUri: Uri?): UploadPictureResult
}