package com.naman.questionbank.pdfviewer.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.naman.questionbank.QuestionBankObject
import com.naman.questionbank.base.ActionType
import com.naman.questionbank.base.fragment.ViewBindingFragment
import com.naman.questionbank.constants.QUERY_PATH
import com.naman.questionbank.databinding.FragmentPdfListBinding
import com.naman.questionbank.pdfviewer.viewmodels.PdfListViewModel
import com.naman.questionbank.pdfviewer.adapters.PdfListAdapter
import com.naman.questionbank.ui.QuestionBankInteractions
import kotlinx.coroutines.launch

class PdfListFragment : ViewBindingFragment<FragmentPdfListBinding>(){

    companion object {
        @JvmStatic
        fun newInstance(bundle: Bundle?) =
            PdfListFragment().apply {
                arguments = bundle ?: Bundle()
            }
    }

    private var viewModel: PdfListViewModel? = null
    private var adapter: PdfListAdapter? = null
    private var qbInteractions : QuestionBankInteractions? = null


    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentPdfListBinding
        get() = FragmentPdfListBinding::inflate

    override fun setup() {
        initView()
        setObservers()
        setListeners()
        getPdfList()
    }

    private fun initView() {
        viewModel = ViewModelProvider(this)[PdfListViewModel::class.java]
        qbInteractions = QuestionBankInteractions(context)
        setupRv()
    }

    private fun setListeners() {
    }

    private fun getPdfList() {
        viewModel?.getPdfList(arguments?.getString(QUERY_PATH,"") ?: "")
    }

    private fun setObservers() {
        viewModel?.pdfList?.observe(viewLifecycleOwner) {
            if (it == null)
                return@observe
            adapter = PdfListAdapter(
                it,
                requireContext()
            )
            binding.pdfListRv.adapter  = adapter
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

    private fun setupRv() {
        binding.pdfListRv.layoutManager = LinearLayoutManager(context)
    }
}