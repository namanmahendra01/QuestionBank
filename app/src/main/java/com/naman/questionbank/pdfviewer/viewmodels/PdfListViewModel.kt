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

class PdfListViewModel : ViewModel(),IFirebaseValueCallback {

    private val _pdfList = MutableLiveData<List<PdfItemData>>()
    val pdfList: LiveData<List<PdfItemData>>
        get() = _pdfList

    private val repository: FeedRepository = FeedRepository()

    init {

        repository.setRepoListener(this)
    }

    fun getPdfList(path: String) {
        repository.getPdfList(path)
    }

    override fun onPdfListFetched(pdfItems: List<PdfItemData>) {
        super.onPdfListFetched(pdfItems)
        viewModelScope.launch(Dispatchers.Default) {
            QuestionBankObject.actionPerformerSharedFlow.emit(
                Envelope(ActionType.CLOSE_PROGRESS_BAR,true)
            )
        }
        _pdfList.postValue(pdfItems)
    }
}
