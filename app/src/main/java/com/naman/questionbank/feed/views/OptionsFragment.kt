package com.naman.questionbank.feed.views

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Spinner
import androidx.appcompat.widget.AppCompatSpinner
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.children
import androidx.core.view.forEach
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.naman.questionbank.base.ExamType
import com.naman.questionbank.base.fragment.ViewBindingFragment
import com.naman.questionbank.constants.PREV_QUERY_PATH
import com.naman.questionbank.constants.QUERY_PATH
import com.naman.questionbank.databinding.FragmentOptionSelectionBinding
import com.naman.questionbank.feed.adapters.OptionsAdapter
import com.naman.questionbank.feed.viewmodels.OptionsViewModel
import com.naman.questionbank.ui.QuestionBankInteractions
import com.naman.questionbank.ui.snippets.SpinnerSnippet

class OptionsFragment : ViewBindingFragment<FragmentOptionSelectionBinding>(){

    companion object {
        @JvmStatic
        fun newInstance(bundle: Bundle?) =
            OptionsFragment().apply {
                arguments = bundle ?: Bundle()
            }
    }

    private var viewModel: OptionsViewModel? = null
    private var adapter: OptionsAdapter? = null
    private var qbInteractions : QuestionBankInteractions? = null


    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentOptionSelectionBinding
        get() = FragmentOptionSelectionBinding::inflate

    override fun setup() {
        initView()
        setObservers()
        setListeners()
        getExamOptions()
    }

    private fun initView() {
        viewModel = ViewModelProvider(this)[OptionsViewModel::class.java]
        qbInteractions = QuestionBankInteractions(context)
        setupRv()
    }

    private fun setListeners() {
        binding.findBtn.setOnClickListener {
            val queryList = mutableListOf(arguments?.getString(PREV_QUERY_PATH,"") ?: "")
            binding.optionRv.forEach {
                val spinner = it as? SpinnerSnippet
                for (view in spinner?.children ?: return@forEach){
                    when(view) {
                        is ConstraintLayout-> {
                            for (view2 in view.children) {
                                when (view2) {
                                    is LinearLayout -> {
                                        queryList.add((view2.getChildAt(1) as? AppCompatSpinner)?.selectedItem.toString())
                                    }
                                }
                            }
                        }
                    }
                }
            }

            qbInteractions?.onFindButtonClicked(queryList)
        }
    }

    private fun getExamOptions() {
        viewModel?.getExamOptions(ExamType.ICSE)
    }

    private fun setObservers() {
        viewModel?.optionList?.observe(viewLifecycleOwner) {

            if (it == null)
                return@observe

            adapter = OptionsAdapter(
                    it,
            requireContext()
            )

            binding.optionRv.adapter  = adapter
        }
    }

    private fun setupRv() {
        binding.optionRv.layoutManager = LinearLayoutManager(context)
    }

    interface OptionFragmentInteraction{
        fun onFindButtonClicked(queryList : List<String>){}
    }
}