package com.naman.questionbank.feed.repository

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.GenericTypeIndicator
import com.google.firebase.database.ValueEventListener
import com.naman.questionbank.QuestionBankObject.examCategoriesDbRef
import com.naman.questionbank.QuestionBankObject.pdfReference
import com.naman.questionbank.base.ExamType
import com.naman.questionbank.ui.snippetData.ExamCategoryCardData
import com.naman.questionbank.ui.snippetData.PdfItemData
import com.naman.questionbank.ui.snippetData.SpinnerData


class FeedRepository {
    private var firebaseValueCallback: IFirebaseValueCallback? = null

    companion object {
        private var examCategoryCardDataList: List<ExamCategoryCardData>? = null
    }

    fun getExamCategoryList() {
        examCategoriesDbRef?.ref?.addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val examType = object : GenericTypeIndicator<List<ExamCategoryCardData>>() {}
                examCategoryCardDataList = dataSnapshot.getValue(examType)
                firebaseValueCallback?.onExamCategoryDataReceived(examCategoryCardDataList)
            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }

    fun setRepoListener(examCategoryCardDataListener: IFirebaseValueCallback) {
        this.firebaseValueCallback = examCategoryCardDataListener
    }

    fun getOptionsForExam(examType: ExamType): List<SpinnerData>? {
        val list = examCategoryCardDataList?.find { examCategoryCardData ->
            examCategoryCardData.id == examType.name
        }
        return list?.options
    }

    fun getPdfList(path: String) {
        val pdfReference = pdfReference?.child(path)
        val pdfItems: MutableList<PdfItemData> = mutableListOf()

        pdfReference?.listAll()?.addOnSuccessListener {
            it.items.forEach { ref ->
                pdfItems.add(PdfItemData(title = ref.name))
            }
            firebaseValueCallback?.onPdfListFetched(pdfItems as? List<PdfItemData> ?: listOf())
        }
    }

    fun getPdfUrl(path: String) {
        val pdfReference = pdfReference?.child(path)
        pdfReference?.downloadUrl?.addOnSuccessListener {
            firebaseValueCallback?.onDownloadUrlFetched(it.toString())
        }
    }
}