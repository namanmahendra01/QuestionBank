package com.naman.questionbank.base.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.view.ContextThemeWrapper
import androidx.lifecycle.Lifecycle
import androidx.viewbinding.ViewBinding

abstract class ViewBindingFragment<VB : ViewBinding> : BaseFragment() {

    private val VIEW_PAGER_FRAGMENT = "view_pager_fragment"

    private var _binding: ViewBinding? = null
    abstract val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> VB

    private var isFirstLoad = true

    open fun customTheme(): Int? {
        return null
    }

    @Suppress("UNCHECKED_CAST")
    protected val binding: VB
        get() = _binding as VB

    open fun getToolbarBaseLayout(): LinearLayout? {
        return null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val customThemeInflater: LayoutInflater? = customTheme()?.let { theme ->
            val contextThemeWrapper: Context = ContextThemeWrapper(requireActivity(), theme)
            inflater.cloneInContext(contextThemeWrapper)
        }
        _binding = bindingInflater.invoke(customThemeInflater ?: inflater, container, false)

        var contentView = requireNotNull(_binding).root

        getToolbarBaseLayout()?.let {
            it.addView(
                contentView, ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
            )
            contentView = it.rootView
        }
        return contentView
    }

    private fun onFragmentLoaded() {
        setup()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val isForViewPager = arguments?.get(VIEW_PAGER_FRAGMENT)
        if (isForViewPager == null || isForViewPager == false) {
            onFragmentLoaded()
        }
    }

    override fun onResume() {
        super.onResume()
        if (isFirstLoad) {
            if (arguments?.get(VIEW_PAGER_FRAGMENT) == true) {
                onFragmentLoaded()
            }
            isFirstLoad = false
        }
    }

    abstract fun setup()

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun isInResumedState(): Boolean {
        return (activity != null && activity?.isFinishing != true && !isDetached && lifecycle.currentState.isAtLeast(
            Lifecycle.State.RESUMED
        ))
    }

    fun isInCreatedState(): Boolean {
        return (activity != null && activity?.isFinishing != true && !isDetached && lifecycle.currentState.isAtLeast(
            Lifecycle.State.CREATED
        ))
    }
}