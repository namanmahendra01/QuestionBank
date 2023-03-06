package com.naman.questionbank.feed.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.naman.questionbank.base.fragment.ViewBindingFragment
import com.naman.questionbank.databinding.FragmentFeedBinding
import com.naman.questionbank.feed.FeedAdapter
import com.naman.questionbank.feed.FeedViewModel

class FeedFragment : ViewBindingFragment<FragmentFeedBinding>(){

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
        setupRv()
        setObservers()
        init()
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
    }

    private fun setupRv() {
        binding.feedRv.layoutManager = GridLayoutManager(context, 2)
    }
}