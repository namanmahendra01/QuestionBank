package com.naman.questionbank.feed

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.naman.questionbank.ui.snippetData.ExamCategoryCardData

class FeedViewModel : ViewModel() {

    private val _examCategoryList  = MutableLiveData<List<ExamCategoryCardData>>()
    val examCategoryList : LiveData<List<ExamCategoryCardData>>
    get() = _examCategoryList


    fun getExamCategoryList(){
     _examCategoryList.postValue(listOf(ExamCategoryCardData(title = "naman")))
    }

}