package com.naman.questionbank.ui.snippets

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.naman.questionbank.databinding.LoadingOverlayBinding
import com.naman.questionbank.databinding.PdfListItemBinding
import com.naman.questionbank.ui.ISnippetSetData
import com.naman.questionbank.ui.QuestionBankInteractions
import com.naman.questionbank.ui.snippetData.LoadingOverlayData
import com.naman.questionbank.ui.snippetData.PdfItemData


class LoadingOverlaySnippet @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttrs: Int = 0,
    private val interaction: QuestionBankInteractions? = null
) : ConstraintLayout(context,attrs,defStyleAttrs), ISnippetSetData<LoadingOverlayData> {

    private val binding: LoadingOverlayBinding =
        LoadingOverlayBinding.inflate(
            LayoutInflater.from(context),
            this,
            true
        )

    init {

    }

    override fun setData(t: LoadingOverlayData?) {
        t ?: return

        if (t.enable){
            binding.rootContainer.visibility = View.VISIBLE
        }else{
            binding.rootContainer.visibility = View.GONE
        }
    }
}
