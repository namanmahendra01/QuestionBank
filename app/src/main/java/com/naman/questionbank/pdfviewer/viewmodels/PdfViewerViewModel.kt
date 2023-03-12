package com.naman.questionbank.pdfviewer.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.naman.questionbank.QuestionBankObject
import com.naman.questionbank.base.ActionType
import com.naman.questionbank.base.Envelope
import com.naman.questionbank.feed.repository.FeedRepository
import com.naman.questionbank.feed.repository.IFirebaseValueCallback
import com.naman.questionbank.ui.snippetData.PdfItemData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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
}