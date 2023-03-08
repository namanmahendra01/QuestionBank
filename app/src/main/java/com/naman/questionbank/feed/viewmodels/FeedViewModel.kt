package com.naman.questionbank.feed.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.naman.questionbank.feed.repository.FeedRepository
import com.naman.questionbank.feed.repository.IFirebaseValueCallback
import com.naman.questionbank.ui.snippetData.ExamCategoryCardData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FeedViewModel : ViewModel(), IFirebaseValueCallback {

    private val _examCategoryList = MutableLiveData<List<ExamCategoryCardData>>()
    val examCategoryList: LiveData<List<ExamCategoryCardData>>
        get() = _examCategoryList

    private val repository: FeedRepository = FeedRepository()

    init {
        repository.setRepoListener(this)
    }

    fun getExamCategoryList() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getExamCategoryList()
        }
    }

    override fun onExamCategoryDataReceived(data: List<ExamCategoryCardData>?) {
        data.let {
            _examCategoryList.postValue(it)
        }
    }
}