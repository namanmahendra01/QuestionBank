package com.naman.questionbank.ui

import android.content.Context
import com.naman.questionbank.QuestionBankObject.actionPerformerSharedFlow
import com.naman.questionbank.base.ActionType
import com.naman.questionbank.base.Envelope
import com.naman.questionbank.feed.views.OptionsFragment
import com.naman.questionbank.ui.snippetData.ExamCategoryCardData
import com.naman.questionbank.ui.snippets.ExamCategoryCardSnippet
import com.naman.questionbank.ui.snippets.PdfItemSnippet
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class QuestionBankInteractions(context: Context?) :
    ExamCategoryCardSnippet.IExamCategoryInteraction, OptionsFragment.OptionFragmentInteraction,
    PdfItemSnippet.PdfItemInteraction {
    private var context: Context? = null

    init {
        this.context = context
    }

    override fun onExamCategoryClicked(examCategoryCardData: ExamCategoryCardData?) {
        sendActionPerformerEvent(
            Envelope(
                ActionType.OPEN_OPTION_FRAGMENT,
                examCategoryCardData
            )
        )
    }

    override fun onFindButtonClicked(queryList: List<String>) {
        sendActionPerformerEvent(
            Envelope(
                ActionType.OPEN_PDF_ACTIVITY,
                queryList
            )
        )

    }
    override fun onPdfItemClicked(fileName: String?) {
        sendActionPerformerEvent(
            Envelope(
                ActionType.OPEN_PDF_VIEWER_FRAGMENT,
                fileName
            )
        )
    }

    private fun sendActionPerformerEvent(envelope: Envelope) {
        GlobalScope.launch(Dispatchers.Default) {
            actionPerformerSharedFlow.emit(
                envelope
            )
        }
    }
}