package com.naman.questionbank.feed.repository

import com.naman.questionbank.ui.snippetData.ExamCategoryCardData

interface IFirebaseValueCallback {
    fun onExamCategoryDataReceived(data: List<ExamCategoryCardData>?){}
}