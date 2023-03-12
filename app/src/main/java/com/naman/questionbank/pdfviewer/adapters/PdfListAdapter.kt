package com.naman.questionbank.pdfviewer.adapters

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.naman.questionbank.ui.QuestionBankInteractions
import com.naman.questionbank.ui.snippetData.PdfItemData
import com.naman.questionbank.ui.snippets.PdfItemSnippet

class PdfListAdapter(
    private val pdfList: List<PdfItemData>,
    private val context: Context
) :
    RecyclerView.Adapter<PdfListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = PdfItemSnippet(context, QuestionBankInteractions(context))
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val examCategoryCardData = pdfList[position]
        holder.pdfItemView?.setData(examCategoryCardData)
    }

    override fun getItemCount(): Int {
        return pdfList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val pdfItemView = itemView as? PdfItemSnippet
    }
}