package com.naman.questionbank.ui.snippets

import android.content.Context
import android.view.LayoutInflater
import androidx.cardview.widget.CardView
import com.naman.questionbank.databinding.ExamCategoryCardBinding
import com.naman.questionbank.ui.ISnippetSetData
import com.naman.questionbank.ui.snippetData.ExamCategoryCardData


class ExamCategoryCardSnippet constructor(
    context: Context,
) : CardView(context), ISnippetSetData<ExamCategoryCardData> {

    private val binding: ExamCategoryCardBinding =
        ExamCategoryCardBinding.inflate(
            LayoutInflater.from(context),
            this,
            true
        )

    override fun setData(t: ExamCategoryCardData) {
        binding.examTitle.text = t.title

    }
}
