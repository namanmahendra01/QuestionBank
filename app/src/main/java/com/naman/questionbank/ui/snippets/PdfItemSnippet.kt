package com.naman.questionbank.ui.snippets

import android.content.Context
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.naman.questionbank.databinding.PdfListItemBinding
import com.naman.questionbank.ui.ISnippetSetData
import com.naman.questionbank.ui.QuestionBankInteractions
import com.naman.questionbank.ui.snippetData.PdfItemData


class PdfItemSnippet constructor(
    context: Context,
    private val interaction: QuestionBankInteractions? = null
) : ConstraintLayout(context), ISnippetSetData<PdfItemData> {

    private val binding: PdfListItemBinding =
        PdfListItemBinding.inflate(
            LayoutInflater.from(context),
            this,
            true
        )

    private var currentData : PdfItemData? = null

    init {
        this.layoutParams = LayoutParams(
            LayoutParams.MATCH_PARENT,
            LayoutParams.WRAP_CONTENT
        )
    }

    private fun setListeners() {
        binding.pdfItemContainer.setOnClickListener {
            interaction?.onPdfItemClicked(currentData?.title)
        }
    }

    override fun setData(t: PdfItemData?) {
        t ?: return
        setListeners()
        currentData = t

        binding.title.text = currentData?.title

    }
interface PdfItemInteraction{
    fun onPdfItemClicked(fileName : String?){}
}
}
