package com.naman.questionbank

import android.app.Application
import com.google.firebase.FirebaseApp

class ApplicationClass : Application() {

    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(applicationContext)
        QuestionBankObject
    }
}