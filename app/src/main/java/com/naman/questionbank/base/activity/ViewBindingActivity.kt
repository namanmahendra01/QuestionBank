package com.naman.questionbank.base.activity

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.LayoutInflater
import androidx.lifecycle.Lifecycle
import androidx.viewbinding.ViewBinding

abstract class ViewBindingActivity<VB : ViewBinding> : BaseActivity() {

    private var _binding: ViewBinding? = null
    abstract val bindingInflater: (LayoutInflater) -> VB

    @Suppress("UNCHECKED_CAST")
    protected val binding: VB
        get() = _binding as VB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = bindingInflater.invoke(layoutInflater)
        setContentView(requireNotNull(_binding).root)
        setup()
    }

    fun setScreenOrientationForTransparentActivity() {
        if (android.os.Build.VERSION.SDK_INT != android.os.Build.VERSION_CODES.O){
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    abstract fun setup()

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    fun isInResumedState(): Boolean {
        return lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED)
    }

    protected fun isInStartedState(): Boolean {
        return lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)
    }
}