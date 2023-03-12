package com.naman.questionbank.pdfviewer.views

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.lifecycle.lifecycleScope
import com.naman.questionbank.QuestionBankObject.actionPerformerSharedFlow
import com.naman.questionbank.R
import com.naman.questionbank.base.ActionType
import com.naman.questionbank.base.activity.ViewBindingActivity
import com.naman.questionbank.common.FragmentStackMethod
import com.naman.questionbank.common.openFragment
import com.naman.questionbank.constants.QUERY_PATH
import com.naman.questionbank.constants.URL_KEY
import com.naman.questionbank.databinding.ActivityPdfBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class PdfActivity : ViewBindingActivity<ActivityPdfBinding>() {


    companion object {
        @JvmStatic
        fun openPdfActivity(context: Context, queryPath: String) {
            val intent = Intent(context, PdfActivity::class.java)
            intent.putExtra(QUERY_PATH, queryPath)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pdf)
    }

    override val bindingInflater: (LayoutInflater) -> ActivityPdfBinding
        get() = ActivityPdfBinding::inflate

    override fun setup() {
        setObservers()
        addPdfListFragment()
    }

    private fun addPdfListFragment() {
        supportFragmentManager.openFragment(
            PdfListFragment.newInstance(bundle = intent.extras),
            binding.pdfListContainer.id,
            FragmentStackMethod.REPLACE,
        )
    }
    private fun openPdfViewerFragment(fileName: String?) {
        supportFragmentManager.openFragment(
            PdfViewerFragment.newInstance(bundle = intent.extras?.apply { putString(QUERY_PATH,intent.getStringExtra(
                QUERY_PATH)+"/"+fileName)
            }
            ),
            binding.pdfViewerContainer.id,
            FragmentStackMethod.ADD,
            addToBackStack = true
        )
    }
    private fun setObservers() {
        lifecycleScope.launch {
            actionPerformerSharedFlow.collectLatest { envelope ->
                when (envelope.action) {
                  ActionType.OPEN_PDF_VIEWER_FRAGMENT -> openPdfViewerFragment(envelope.data as? String)
                    else -> {}
                }
            }
        }
    }
}