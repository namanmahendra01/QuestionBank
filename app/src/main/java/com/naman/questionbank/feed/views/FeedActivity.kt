package com.naman.questionbank.feed.views

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.naman.questionbank.QuestionBankObject.actionPerformerSharedFlow
import com.naman.questionbank.R
import com.naman.questionbank.base.ActionType
import com.naman.questionbank.base.activity.ViewBindingActivity
import com.naman.questionbank.common.FragmentStackMethod
import com.naman.questionbank.common.openFragment
import com.naman.questionbank.constants.PREV_QUERY_PATH
import com.naman.questionbank.databinding.ActivityMainBinding
import com.naman.questionbank.pdfviewer.views.PdfActivity
import com.naman.questionbank.ui.snippetData.ExamCategoryCardData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FeedActivity : ViewBindingActivity<ActivityMainBinding>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override val bindingInflater: (LayoutInflater) -> ActivityMainBinding
        get() = ActivityMainBinding::inflate

    override fun setup() {
        setObservers()
        addFeedFragment()
    }
    private fun setObservers() {
        lifecycleScope.launch {
            actionPerformerSharedFlow.collect { envelope ->
                when (envelope.action) {
                    ActionType.OPEN_OPTION_FRAGMENT -> openSelectionFragment(envelope.data)
                    ActionType.OPEN_PDF_ACTIVITY -> openPdfActivity(envelope.data as? List<String>)
                    else -> {}
                }
            }
        }
    }

    private fun openPdfActivity(queryList: List<String>?) {
      val path : String = queryList?.joinToString("/") ?: return
      PdfActivity.openPdfActivity(this,path)
    }

    private fun addFeedFragment() {
        supportFragmentManager.openFragment(
            FeedFragment.newInstance(bundle = intent.extras),
            binding.feedContainer.id,
            FragmentStackMethod.REPLACE,
            addToBackStack = false
        )
    }

    private fun openSelectionFragment(data: Any?) {
        val examCategoryCardData = data as? ExamCategoryCardData
        supportFragmentManager.openFragment(
            OptionsFragment.newInstance(bundle = intent.extras?.apply {
                putString(PREV_QUERY_PATH,examCategoryCardData?.id)
            } ?: kotlin.run {
                Bundle().apply {
                    this.putString(PREV_QUERY_PATH,examCategoryCardData?.id)
                }
            }

            ),
            binding.optionContainer.id,
            FragmentStackMethod.REPLACE,
            addToBackStack = true
        )
    }
}