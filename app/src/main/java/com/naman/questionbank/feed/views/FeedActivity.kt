package com.naman.questionbank.feed.views

import android.os.Bundle
import android.view.LayoutInflater
import androidx.lifecycle.lifecycleScope
import com.naman.questionbank.QuestionBankObject.actionPerformerSharedFlow
import com.naman.questionbank.R
import com.naman.questionbank.base.ActionType
import com.naman.questionbank.base.activity.ViewBindingActivity
import com.naman.questionbank.common.FragmentStackMethod
import com.naman.questionbank.common.openFragment
import com.naman.questionbank.databinding.ActivityMainBinding

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
        lifecycleScope.launchWhenResumed {
            actionPerformerSharedFlow.collect { envelope ->
                when (envelope.action) {
                    ActionType.OPEN_OPTION_FRAGMENT -> openSelectionFragment()
                    else -> {}
                }
            }
        }
    }

    private fun addFeedFragment() {
        supportFragmentManager.openFragment(
            FeedFragment.newInstance(bundle = intent.extras),
            binding.flContainer.id,
            FragmentStackMethod.REPLACE,
            addToBackStack = true
        )
    }

    private fun openSelectionFragment() {
        supportFragmentManager.openFragment(
            OptionsFragment.newInstance(bundle = intent.extras),
            binding.flContainer.id,
            FragmentStackMethod.REPLACE,
            addToBackStack = true
        )
    }
}