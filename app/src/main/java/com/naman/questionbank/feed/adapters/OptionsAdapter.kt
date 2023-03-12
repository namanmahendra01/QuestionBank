package com.naman.questionbank.feed.adapters

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.naman.questionbank.ui.QuestionBankInteractions
import com.naman.questionbank.ui.snippetData.SpinnerData
import com.naman.questionbank.ui.snippets.SpinnerSnippet

class OptionsAdapter(
    private val optionList: List<SpinnerData>,
    private val context: Context
) :
    RecyclerView.Adapter<OptionsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = SpinnerSnippet(context, interaction = QuestionBankInteractions(context))
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val spinnerData = optionList[position]
        holder.spinnerView?.setData(spinnerData)
    }

    override fun getItemCount(): Int {
        return optionList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val spinnerView = itemView as? SpinnerSnippet
    }

    fun getGetSelectedItem(){
    }
}