package com.naman.questionbank.ui.snippets

import android.content.Context
import android.view.LayoutInflater
import androidx.cardview.widget.CardView
import com.bumptech.glide.Glide
import com.naman.questionbank.databinding.ExamCategoryCardItemBinding
import com.naman.questionbank.ui.ISnippetSetData
import com.naman.questionbank.ui.QuestionBankInteractions
import com.naman.questionbank.ui.snippetData.ExamCategoryCardData


class ExamCategoryCardSnippet constructor(
    context: Context,
    private val interaction: QuestionBankInteractions? = null
) : CardView(context), ISnippetSetData<ExamCategoryCardData> {

    private val binding: ExamCategoryCardItemBinding =
        ExamCategoryCardItemBinding.inflate(
            LayoutInflater.from(context),
            this,
            true
        )

    private var currentData: ExamCategoryCardData? = null

    init {
        setListeners()
    }

    private fun setListeners() {
        binding.cardContainer.setOnClickListener {
            interaction?.onExamCategoryClicked(currentData)
        }
    }

    override fun setData(t: ExamCategoryCardData?) {
        t ?: return
        currentData = t
        binding.examTitle.text = currentData?.title

        currentData?.imageUrl.let {
            Glide.with(this)
                .load(it)
                .centerCrop()
                .into(binding.examImage)

        }

    }

    interface IExamCategoryInteraction {
        fun onExamCategoryClicked(examCategoryCardData: ExamCategoryCardData?) {}
    }
}
