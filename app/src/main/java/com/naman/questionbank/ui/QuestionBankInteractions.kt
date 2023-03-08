package com.naman.questionbank.ui

import android.content.Context
import android.util.Log
import com.naman.questionbank.QuestionBankObject.actionPerformerSharedFlow
import com.naman.questionbank.base.ActionType
import com.naman.questionbank.base.Envelope
import com.naman.questionbank.feed.views.OptionsFragment
import com.naman.questionbank.ui.snippetData.ExamCategoryCardData
import com.naman.questionbank.ui.snippets.ExamCategoryCardSnippet
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class QuestionBankInteractions(context: Context?) :
    ExamCategoryCardSnippet.IExamCategoryInteraction, OptionsFragment.OptionFragmentInteraction {
    private var context: Context? = null

    init {
        this.context = context
    }

    override fun onExamCategoryClicked(examCategoryCardData: ExamCategoryCardData?) {
        CoroutineScope(Dispatchers.IO).launch {
            actionPerformerSharedFlow.emit(
                Envelope(
                    ActionType.OPEN_OPTION_FRAGMENT,
                    examCategoryCardData
                )
            )

        }

    }

    override fun onFindButtonClicked(queryList: List<String>) {
        Log.d("naman", "onFindButtonClicked:$queryList ")
    }
}