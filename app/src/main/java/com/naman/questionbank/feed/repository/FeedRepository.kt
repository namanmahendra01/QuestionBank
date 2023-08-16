package com.naman.questionbank.feed.repository

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.GenericTypeIndicator
import com.google.firebase.database.ValueEventListener
import com.naman.questionbank.QuestionBankObject
import com.naman.questionbank.QuestionBankObject.examCategoriesDbRef
import com.naman.questionbank.QuestionBankObject.paymentDbRef
import com.naman.questionbank.QuestionBankObject.pdfReference
import com.naman.questionbank.QuestionBankObject.userDetails
import com.naman.questionbank.constants.IS_SUBSCRIBED
import com.naman.questionbank.constants.TRIAL_COUNT_KEY
import com.naman.questionbank.payment.UserDetailsModel
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

    fun getOptionsForExam(examType: String): List<SpinnerData>? {
        val list = examCategoryCardDataList?.find { examCategoryCardData ->
            examCategoryCardData.id == examType
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

    fun addPaymentModel(userDetailsModel: UserDetailsModel) {
        val uuidRef =  paymentDbRef?.child(userDetailsModel.uid.toString())
        uuidRef?.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists().not()) {
                    uuidRef.setValue(userDetailsModel)
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    fun updateTrialCountInFirebaseAndLocal() {
        val uuidRef = QuestionBankObject.uid?.let { paymentDbRef?.child(it) }
        userDetails?.trialCount = (userDetails?.trialCount ?: 0) + 1
        val updatedTrialCount = userDetails?.trialCount
        uuidRef?.child(TRIAL_COUNT_KEY)?.setValue(updatedTrialCount)
    }

    fun updateUserPurchase() {
        val uuidRef = QuestionBankObject.uid?.let { paymentDbRef?.child(it) }
        uuidRef?.child(IS_SUBSCRIBED)?.setValue(true)
        userDetails?.isSubscribed = true
    }
}