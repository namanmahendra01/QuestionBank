package com.naman.questionbank.feed.views

import android.os.Bundle
import android.view.LayoutInflater
import com.naman.questionbank.R
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
        addFeedFragment()
    }

    private fun addFeedFragment() {
        supportFragmentManager.openFragment(
            FeedFragment.newInstance(bundle = intent.extras),
            binding.flContainer.id,
            FragmentStackMethod.REPLACE
        )
    }
}