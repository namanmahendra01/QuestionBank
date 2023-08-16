package com.naman.questionbank.pdfviewer.views

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.naman.questionbank.QuestionBankObject
import com.naman.questionbank.base.ActionType
import com.naman.questionbank.base.fragment.ViewBindingFragment
import com.naman.questionbank.constants.QUERY_PATH
import com.naman.questionbank.constants.URL_KEY
import com.naman.questionbank.databinding.FragmentPdfViewerBinding
import com.naman.questionbank.payment.PaymentActivity
import com.naman.questionbank.pdfviewer.viewmodels.PdfViewerViewModel
import com.naman.questionbank.ui.snippetData.PdfViewerData
import kotlinx.coroutines.launch

class PdfViewerFragment : ViewBindingFragment<FragmentPdfViewerBinding>(){

    companion object {
        @JvmStatic
        fun newInstance(bundle: Bundle?) =
            PdfViewerFragment().apply {
                arguments = bundle ?: Bundle()
            }
    }

    private var viewModel: PdfViewerViewModel? = null

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentPdfViewerBinding
        get() = FragmentPdfViewerBinding::inflate

    override fun setup() {
        viewModel = ViewModelProvider(this)[PdfViewerViewModel::class.java]
        initView()
        setObservers()
        openPdf()
        getPdfUrl()
        setListeners()
    }

    private fun setListeners() {
        binding.paymentBtn.setOnClickListener {
            val i = Intent(context,PaymentActivity::class.java)
            startActivity(i)
        }
    }

    private fun updateTrialCount() {
        viewModel?.updateTrialCount()
    }

    private fun getPdfUrl(){
        viewModel?.getPdfUrl(arguments?.getString(QUERY_PATH,"") ?: "")
    }

    private fun openPdf() {
        binding.pdfView.setData(PdfViewerData(url = arguments?.getString(URL_KEY,"")))
        updateTrialCount()
    }

    private fun initView() {
//        if ((QuestionBankObject.userDetails?.trialCount
//                ?: 0) > 3 && QuestionBankObject.userDetails?.isSubscribed == false
//        ) {
//            binding.lockedScreen.visibility = View.VISIBLE
//        } else {
//            binding.lockedScreen.visibility = View.GONE
//
//        }
    }

    override fun onResume() {
        super.onResume()
        initView()
    }

    private fun setObservers() {

        viewModel?.pdfUrl?.observe(viewLifecycleOwner) {
            if (it == null)
                return@observe
            binding.pdfView.setData(PdfViewerData(url = it))
        }

        lifecycleScope.launch {
            QuestionBankObject.actionPerformerSharedFlow.collect { envelope ->
                when (envelope.action) {
                    ActionType.CLOSE_PROGRESS_BAR->{
                        binding.progressBar.visibility = View.GONE
                    }
                    else -> {}
                }
            }
        }
    }
}