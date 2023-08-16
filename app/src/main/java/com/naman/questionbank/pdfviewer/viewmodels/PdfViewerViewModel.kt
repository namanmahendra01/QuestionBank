package com.naman.questionbank.pdfviewer.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.naman.questionbank.feed.repository.FeedRepository
import com.naman.questionbank.feed.repository.IFirebaseValueCallback

class PdfViewerViewModel : ViewModel(),IFirebaseValueCallback {

    private val _pdfUrl = MutableLiveData<String>()
    val pdfUrl: LiveData<String>
        get() = _pdfUrl

    private val repository: FeedRepository = FeedRepository()

    init {

        repository.setRepoListener(this)
    }

    fun getPdfUrl(path: String) {
        repository.getPdfUrl(path)
    }

    override fun onDownloadUrlFetched(url: String) {
        _pdfUrl.postValue(url)
    }

    fun updateTrialCount() {
        repository.updateTrialCountInFirebaseAndLocal()
    }

    fun updatePurchase() {
        repository.updateUserPurchase()
    }
}