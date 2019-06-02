package com.example.myapplication.service

import android.net.Uri
import android.util.Log
import com.example.myapplication.dto.AnalyticsServiceResponse.StatusCheckResponse
import com.example.myapplication.dto.AnalyticsServiceResponse.UploadPictureResponse
import com.example.myapplication.model.AnalyticsServiceModel
import com.example.myapplication.model.AnalyticsServiceModel.AnalysisStatus.*
import com.example.myapplication.model.AnalyticsServiceModel.UploadPictureResult
import com.google.gson.Gson
import khttp.structures.files.FileLike
import java.io.File

class HttpAnalyzeService : AnalyzeService {
    private var gson = Gson()

    private var analysisUrl = "http://google.com"
    private var statusCheckUrl = "http://google.com"
    private var uploadPictureUrl = "http://google.com"

    override fun analyse(userProfile: String): AnalyticsServiceModel.AnalysisStatus {
        val response = khttp.get(
            url = analysisUrl,
            params = mapOf("userProfile" to userProfile)
        )
        return if (response.statusCode == 200) {
            IN_PROGRESS
        } else {
            Log.e("", response.toString())
            ERROR
        }
    }

    override fun checkStatus(userProfile: String): AnalyticsServiceModel.AnalysisStatus {
        val response = khttp.get(url = statusCheckUrl, params = mapOf("userProfile" to userProfile))
        return if (response.statusCode == 200) {
            return try {
                val statusCheckResponse = gson.fromJson(response.text, StatusCheckResponse::class.java)
                valueOf(statusCheckResponse.status)
            } catch (ex: Exception) {
                Log.d("", "Failed to get status", ex)
                ERROR
            }
        } else {
            Log.e("", response.toString())
            ERROR
        }
    }

    override fun uploadPicture(userProfile: String, imageUri: Uri?): UploadPictureResult {
        if (imageUri != null && imageUri.path != null) {
            val response = khttp.post(
                url = uploadPictureUrl,
                params = mapOf("userProfile" to userProfile),
                files = listOf(FileLike("file", File(imageUri.path)))
            )
            if (response.statusCode != 200) {
                Log.e("", "upload failed: $response")
                return UploadPictureResult(0, ERROR)
            }

            try {
                val uploadPictureResponse = gson.fromJson(response.text, UploadPictureResponse::class.java)
                return UploadPictureResult(uploadPictureResponse.likes, COMPLETE)
            } catch (ex: Exception) {
                Log.e("", "Failed to upload image", ex)
            }
        }

        return UploadPictureResult(0, ERROR)
    }
}
