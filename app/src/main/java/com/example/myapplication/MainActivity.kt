package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.Toast
import com.example.myapplication.model.AnalyticsServiceModel.AnalysisStatus
import com.example.myapplication.model.AnalyticsServiceModel.AnalysisStatus.*
import com.example.myapplication.service.DemoAnalyzeService
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    companion object {
        var status: AnalysisStatus = NO
        var analyticsService = DemoAnalyzeService()
        var currentUserProfile = ""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        instaProfileText.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {
                currentUserProfile = s.toString()
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                status = NO
                analysisStatus.text = ""
                analyzeBnt.text = "Analyze!"
            }
        })
    }

    fun analyze(view: View) {
        val instaProfile = instaProfileText.text.toString()

        if (instaProfile.isBlank()) {
            Toast.makeText(this, "Please enter Instagram profile ", Toast.LENGTH_SHORT).show()
            return
        }

        if (status == NO) {
            AnalyzeProfileCallTask(this).execute()
            return
        }

        if (status == IN_PROGRESS) {
            CheckAnalysisStatusCallTask(this).execute()
            return
        }

        if (status == COMPLETE) {
            val goToPhotosIntent = Intent(this, PhotosActivity::class.java)
            goToPhotosIntent.putExtra(PhotosActivity.INSTA_PROFILE_EXTRA_PARAM_NAME, instaProfile)
            startActivity(goToPhotosIntent)
        }
    }

    @SuppressLint("StaticFieldLeak")
    class AnalyzeProfileCallTask(private var activity: MainActivity) : AsyncTask<String, String, AnalysisStatus>() {

        override fun onPreExecute() {
            super.onPreExecute()
            setStatusText("Analysis in progress...")
            Companion.status = IN_PROGRESS
            activity.analyzeBnt.isEnabled = false
            setBntText("Check status")
        }

        override fun doInBackground(vararg p0: String?): AnalysisStatus {
            return try {
                analyticsService.analyse(currentUserProfile)
            } catch (ex: Exception) {
                Log.d("", "Error in doInBackground " + ex.message)
                ERROR
            }
        }

        override fun onPostExecute(result: AnalysisStatus?) {
            if (result == null || result == ERROR) {
                setStatusText("Something went wrong... =(")
                activity.analyzeBnt.isEnabled = true
                Companion.status = NO
                setBntText("Analyze!")
                return
            }

            if (result == USER_NOT_FOUND) {
                setStatusText("Profile not found")
                Companion.status = NO
                setBntText("Analyze!")
            }

            activity.analyzeBnt.isEnabled = true
        }

        private fun setStatusText(text: String) {
            activity.analysisStatus.text = text
        }

        private fun setBntText(text: String) {
            activity.analyzeBnt.text = text
        }
    }

    @SuppressLint("StaticFieldLeak")
    class CheckAnalysisStatusCallTask(private var activity: MainActivity) : AsyncTask<String, String, AnalysisStatus>() {

        override fun onPreExecute() {
            super.onPreExecute()
            activity.analyzeBnt.isEnabled = false
        }

        override fun doInBackground(vararg p0: String?): AnalysisStatus {
            return try {
                analyticsService.checkStatus(currentUserProfile)
            } catch (ex: Exception) {
                Log.d("", "Error in doInBackground " + ex.message)
                ERROR
            }
        }

        override fun onPostExecute(result: AnalysisStatus?) {
            if (result != null && result != ERROR) {
                if (result == COMPLETE) {
                    Companion.status = COMPLETE
                    activity.analyzeBnt.text = "Upload more!"
                    activity.analysisStatus.text = "Analysis complete!"
                }
            }
            activity.analyzeBnt.isEnabled = true
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        return true
    }
}
