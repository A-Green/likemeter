package com.example.myapplication

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import com.example.myapplication.service.DemoAnalyzeService
import com.example.myapplication.service.HttpAnalyzeService
import kotlinx.android.synthetic.main.activity_photos_activiy.*
import java.util.*

class PhotosActivity : AppCompatActivity() {

    private val analysisService = DemoAnalyzeService()

    companion object {
        var imageUrl: Uri? = Uri.EMPTY
        var imagePickRequestCode = 100
        var INSTA_PROFILE_EXTRA_PARAM_NAME = "INSTA_PROFILE"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photos_activiy)
    }

    fun pickImage(view: View?) {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        startActivityForResult(intent, imagePickRequestCode)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK && requestCode == 100) {
            imageUrl = data?.data
            selectedImageView.setImageURI(imageUrl)
        }

        try {
            var uploadPictureResult = analysisService.uploadPicture("userProfile", data?.data)
            likesCountText.text = uploadPictureResult.likes.toString()

        } catch (ex: Exception) {
            Log.e("", "Upload failed", ex)
        }
    }
}
