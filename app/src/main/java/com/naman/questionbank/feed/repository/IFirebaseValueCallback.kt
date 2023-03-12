package com.naman.questionbank.feed.repository

import com.naman.questionbank.ui.snippetData.ExamCategoryCardData
import com.naman.questionbank.ui.snippetData.PdfItemData

interface IFirebaseValueCallback {
    fun onExamCategoryDataReceived(data: List<ExamCategoryCardData>?){}
    fun onDownloadUrlFetched(url:String){}
    fun onPdfListFetched(pdfItems: List<PdfItemData>){}
}