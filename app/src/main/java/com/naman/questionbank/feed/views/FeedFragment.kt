package com.naman.questionbank.feed.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.naman.questionbank.QuestionBankObject
import com.naman.questionbank.base.ActionType
import com.naman.questionbank.base.fragment.ViewBindingFragment
import com.naman.questionbank.databinding.FragmentFeedBinding
import com.naman.questionbank.feed.adapters.FeedAdapter
import com.naman.questionbank.feed.viewmodels.FeedViewModel
import com.naman.questionbank.payment.UserDetailsModel
import kotlinx.coroutines.launch

class FeedFragment : ViewBindingFragment<FragmentFeedBinding>() {

    companion object {
        @JvmStatic
        fun newInstance(bundle: Bundle?) =
            FeedFragment().apply {
                arguments = bundle ?: Bundle()
            }
    }

    private var viewModel: FeedViewModel? = null

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentFeedBinding
        get() = FragmentFeedBinding::inflate

    override fun setup() {
        viewModel = ViewModelProvider(this)[FeedViewModel::class.java]
        storeDeviceId()
        setupRv()
        setObservers()
        init()
    }

    private fun storeDeviceId() {
            viewModel?.storeDeviceId(UserDetailsModel(uid = QuestionBankObject.uid))
    }

    private fun init() {
        viewModel?.getExamCategoryList()
    }

    private fun setObservers() {
        viewModel?.examCategoryList?.observe(viewLifecycleOwner) {
            binding.feedRv.adapter = FeedAdapter(
                it,
                requireContext()
            )
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
        binding.feedRv.layoutManager = GridLayoutManager(context, 2)
    }
}