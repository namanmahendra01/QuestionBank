package com.naman.questionbank

import android.app.Application
import android.util.Log
import com.downloader.PRDownloader
import com.google.firebase.FirebaseApp

class ApplicationClass : Application() {

    override fun onCreate() {
        super.onCreate()
        QuestionBankObject.initialize(this)
    }
}