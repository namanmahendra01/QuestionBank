package com.naman.questionbank.feed.repository

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.GenericTypeIndicator
import com.google.firebase.database.ValueEventListener
import com.naman.questionbank.QuestionBankObject.examCategoriesDbRef
import com.naman.questionbank.base.ExamType
import com.naman.questionbank.ui.snippetData.ExamCategoryCardData
import com.naman.questionbank.ui.snippetData.SpinnerData
import kotlin.math.log


class FeedRepository {
    private var examCategoryCardDataListener: IFirebaseValueCallback? = null

    companion object {
        private var examCategoryCardDataList: List<ExamCategoryCardData>? = null
    }

    fun getExamCategoryList() {
        Log.d("naman", "    fun getExamCategoryList() ")

        examCategoriesDbRef?.ref?.addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val examType = object : GenericTypeIndicator<List<ExamCategoryCardData>>() {}
                examCategoryCardDataList = dataSnapshot.getValue(examType)
                examCategoryCardDataListener?.onExamCategoryDataReceived(examCategoryCardDataList)
                Log.d("naman", "onDataChange: $examCategoryCardDataList")

            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("naman", "onCancelled: $error ")
            }
        })
    }

    fun setRepoListener(examCategoryCardDataListener: IFirebaseValueCallback) {
        this.examCategoryCardDataListener = examCategoryCardDataListener
    }

    fun getOptionsForExam(examType: ExamType): List<SpinnerData>? {
        val list = examCategoryCardDataList?.find { examCategoryCardData ->
            examCategoryCardData.id == examType.name
        }
        return list?.options
    }
}