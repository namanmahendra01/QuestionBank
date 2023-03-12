package com.naman.questionbank.ui.snippets

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.constraintlayout.widget.ConstraintLayout
import com.naman.questionbank.R
import com.naman.questionbank.databinding.SpinnerItemBinding
import com.naman.questionbank.ui.ISnippetSetData
import com.naman.questionbank.ui.QuestionBankInteractions
import com.naman.questionbank.ui.snippetData.SpinnerData


class SpinnerSnippet @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttrs: Int = 0,
    private val interaction: QuestionBankInteractions? = null
) : ConstraintLayout(context,attrs,defStyleAttrs), ISnippetSetData<SpinnerData> {

    private val binding: SpinnerItemBinding =
        SpinnerItemBinding.inflate(
            LayoutInflater.from(context),
            this,
            true
        )

    init {
        setListeners()
        this.layoutParams = LayoutParams(
            LayoutParams.MATCH_PARENT,
            LayoutParams.WRAP_CONTENT
        )
    }

    private fun setListeners() {}

    @SuppressLint("SuspiciousIndentation")
    override fun setData(t: SpinnerData?) {
        t ?: return

       val adapter =  ArrayAdapter(context, R.layout.spinner_item_text,getArrayListFromList(t.items))
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
    fun View.setWidth(width: Float) {
        var params = layoutParams
        if (params == null) {
            params = ViewGroup.LayoutParams(width.toInt(), height)
        } else {
            params.width = width.toInt()
        }
        layoutParams = params
    }
}
