package com.naman.questionbank.ui.snippets

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.widget.ArrayAdapter
import androidx.constraintlayout.widget.ConstraintLayout
import com.naman.questionbank.databinding.SpinnerItemBinding
import com.naman.questionbank.ui.ISnippetSetData
import com.naman.questionbank.ui.QuestionBankInteractions
import com.naman.questionbank.ui.snippetData.ExamCategoryCardData
import com.naman.questionbank.ui.snippetData.SpinnerData


class SpinnerSnippet constructor(
    context: Context,
    private val interaction: QuestionBankInteractions? = null
) : ConstraintLayout(context), ISnippetSetData<SpinnerData> {

    private val binding: SpinnerItemBinding =
        SpinnerItemBinding.inflate(
            LayoutInflater.from(context),
            this,
            true
        )

    init {
        setListeners()
    }

    private fun setListeners() {}

    @SuppressLint("SuspiciousIndentation")
    override fun setData(t: SpinnerData?) {
        t ?: return

       val adapter =  ArrayAdapter(context,android.R.layout.simple_spinner_item,getArrayListFromList(t.items))
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinner.adapter = adapter

        binding.title.text = t.title

    }

    private fun getArrayListFromList(list: List<String>?) : ArrayList<String>{
        val arrayList : ArrayList<String> = arrayListOf()
        list?.forEach{
            arrayList.add(it)
        }
        return arrayList
    }

}
