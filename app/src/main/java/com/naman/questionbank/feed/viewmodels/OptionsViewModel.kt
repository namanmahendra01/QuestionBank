package com.naman.questionbank.feed.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.naman.questionbank.base.ExamType
import com.naman.questionbank.feed.repository.FeedRepository
import com.naman.questionbank.ui.snippetData.SpinnerData

class OptionsViewModel : ViewModel() {
    private val _optionsList = MutableLiveData<List<SpinnerData>>()
    val optionList: LiveData<List<SpinnerData>>
        get() = _optionsList

    private val repository: FeedRepository = FeedRepository()

    fun getExamOptions(examType: ExamType) {
        _optionsList.postValue(repository.getOptionsForExam(examType))
    }
}