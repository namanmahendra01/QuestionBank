package com.naman.questionbank.feed.adapters

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.naman.questionbank.ui.QuestionBankInteractions
import com.naman.questionbank.ui.snippetData.ExamCategoryCardData
import com.naman.questionbank.ui.snippets.ExamCategoryCardSnippet

class FeedAdapter(private val examList: List<ExamCategoryCardData>,
                  private val context: Context) :
    RecyclerView.Adapter<FeedAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ExamCategoryCardSnippet(context, QuestionBankInteractions(context))
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val examCategoryCardData = examList[position]
        holder.examCategoryView?.setData(examCategoryCardData)
    }

    override fun getItemCount(): Int {
        return examList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val examCategoryView = itemView as? ExamCategoryCardSnippet
    }
}