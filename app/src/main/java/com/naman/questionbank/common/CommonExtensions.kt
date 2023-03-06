package com.naman.questionbank.common

import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.graphics.Rect
import android.graphics.drawable.GradientDrawable
import android.net.Uri
import android.text.Editable
import android.util.TypedValue
import android.view.HapticFeedbackConstants
import android.view.TouchDelegate
import android.view.View
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.naman.questionbank.base.fragment.BaseFragment
import java.util.*
import java.util.regex.Pattern
import kotlin.math.max
import kotlin.math.min

enum class FragmentStackMethod {
    ADD,
    REPLACE
}

data class ConstraintType(
    var topToTopOf: Int? = null,
    var topToBottomOf: Int? = null,
    var topToLeftOf: Int? = null,
    var topToRightOf: Int? = null,
    var startToEndOf: Int? = null,
    var bottomToBottomOf: Int? = null,
    var startToStartOf: Int? = null,
    var bottomToTopOf: Int? = null,
    var endToEndOf: Int? = null,

    )

fun <T> List<T>.getNotEmpty(): List<T>? {
    if (this.isNotEmpty())
        return this
    return null
}

fun String?.getNotEmpty(): String? {
    return if (this.isNullOrEmpty()) null else this
}

inline fun <T1 : Any, T2 : Any, R : Any> safeLet(p1: T1?, p2: T2?, block: (T1, T2) -> R?): R? {
    return if (p1 != null && p2 != null) block(p1, p2) else null
}

inline fun <T1 : Any, T2 : Any, T3 : Any, R : Any> safeLet(
    p1: T1?,
    p2: T2?,
    p3: T3?,
    block: (T1, T2, T3) -> R?,
): R? {
    return if (p1 != null && p2 != null && p3 != null) block(p1, p2, p3) else null
}

inline fun <T1 : Any, T2 : Any, T3 : Any, T4 : Any, R : Any> safeLet(
    p1: T1?,
    p2: T2?,
    p3: T3?,
    p4: T4?,
    block: (T1, T2, T3, T4) -> R?,
): R? {
    return if (p1 != null && p2 != null && p3 != null && p4 != null) block(p1, p2, p3, p4) else null
}

inline fun <T1 : Any, T2 : Any, T3 : Any, T4 : Any, T5 : Any, R : Any> safeLet(
    p1: T1?,
    p2: T2?,
    p3: T3?,
    p4: T4?,
    p5: T5?,
    block: (T1, T2, T3, T4, T5) -> R?,
): R? {
    return if (p1 != null && p2 != null && p3 != null && p4 != null && p5 != null) block(
        p1,
        p2,
        p3,
        p4,
        p5
    ) else null
}

fun FragmentManager.openFragment(
    fragment: BaseFragment,
    containerId: Int,
    fragmentStackMethod: FragmentStackMethod = FragmentStackMethod.ADD,
    addToBackStack: Boolean = false,
    tag: String? = null,
) {
    beginTransaction().apply {
        when (fragmentStackMethod.name) {
            FragmentStackMethod.REPLACE.name -> replace(containerId, fragment, tag)
            FragmentStackMethod.ADD.name -> add(containerId, fragment, tag)
        }

        if (addToBackStack) {
            addToBackStack(tag)
        }

        commit()
    }
}

@Suppress("UNCHECKED_CAST")
fun <T, U> Map<T, U?>.convertToNonNullableMap(): Map<T, U> {
    return filterValues { it != null } as Map<T, U>
}

fun View.setBackgroundShape(bgColor: Int, shape: Int = GradientDrawable.OVAL) {
    this.background = GradientDrawable().apply {
        setColor(bgColor)
        this.shape = shape
    }
}


fun Context.showToast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, duration).show()
}

fun setStatusBarColorInt(activity: Activity?, color: Int) {
    activity?.let {
        it.window.statusBarColor = color
    }
}

fun View.addRipple() = with(TypedValue()) {
    context.theme.resolveAttribute(android.R.attr.selectableItemBackground, this, true)
    setBackgroundResource(resourceId)
}

fun View.increaseHitArea(dp: Float) {
    val increasedArea = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        dp,
        Resources.getSystem().displayMetrics
    ).toInt()

    val parent = parent as View
    parent.post {
        val rect = Rect()
        getHitRect(rect)
        rect.top -= increasedArea
        rect.left -= increasedArea
        rect.bottom += increasedArea
        rect.right += increasedArea
        parent.touchDelegate = TouchDelegate(rect, this)
    }
}


fun RecyclerView.scrollToPositionIfNotVisible(position: Int) {
    if (position < 0 || position >= (this.layoutManager?.itemCount ?: 0)) return

    (this.layoutManager as? LinearLayoutManager)?.findLastCompletelyVisibleItemPosition()
        ?.let { lastVisibleItemPosition ->
            if (position > lastVisibleItemPosition) {
                this.layoutManager?.scrollToPosition(position)
            }
        }
}

fun RecyclerView.scrollToPositionWithOffset(position: Int, offset: Int = 0) {
    this.layoutManager?.let { layoutManager ->
        when (layoutManager) {
            is StaggeredGridLayoutManager -> {
                layoutManager.scrollToPositionWithOffset(position, offset)
            }

            is LinearLayoutManager -> {
                layoutManager.scrollToPositionWithOffset(position, offset)
            }

            else -> {}
        }
    }
}

fun View.visibleIf(condition: Boolean) {
    when (condition) {
        true -> this.visibility = View.VISIBLE
        else -> this.visibility = View.GONE
    }
}


fun StaggeredGridLayoutManager.getFirstVisibleItemPos(): Int {
    val into = IntArray(this.spanCount)
    this.findFirstVisibleItemPositions(into)
    var visiblePosition = Int.MAX_VALUE
    for (pos in into) {
        visiblePosition = min(pos, visiblePosition)
    }
    return visiblePosition
}

fun StaggeredGridLayoutManager.getLastVisibleItemPos(): Int {
    val into = IntArray(this.spanCount)
    this.findLastVisibleItemPositions(into)
    var visiblePosition = Int.MIN_VALUE
    for (pos in into) {
        visiblePosition = max(pos, visiblePosition)
    }
    return visiblePosition
}

fun RecyclerView?.smoothScrollToPositionWithThreshold(targetItem: Int, threshold: Int = 10) {
    kotlin.runCatching {
        this?.layoutManager?.apply {
            when (this) {
                is LinearLayoutManager -> {
                    smoothScrollWithThreshold(
                        this@smoothScrollToPositionWithThreshold,
                        targetItem,
                        findFirstVisibleItemPosition(),
                        threshold
                    )
                }
                is StaggeredGridLayoutManager -> {
                    smoothScrollWithThreshold(
                        this@smoothScrollToPositionWithThreshold,
                        targetItem,
                        getFirstVisibleItemPos(),
                        threshold
                    )
                }
                else -> smoothScrollToPosition(targetItem)
            }
        }
    }
}

fun smoothScrollWithThreshold(rv: RecyclerView, targetItem: Int, topItem: Int, threshold: Int) {
    val distance = topItem - targetItem
    val anchorItem = when {
        distance > threshold -> targetItem + threshold
        distance < -threshold -> targetItem - threshold
        else -> topItem
    }
    if (anchorItem != topItem) rv.scrollToPosition(anchorItem)
    rv.post {
        if (rv.isAttachedToWindow) {
            rv.smoothScrollToPosition(targetItem)
        }
    }
}

fun View.setConstraints(block: ConstraintType.() -> Unit) {

    if (this.parent !is ConstraintLayout) {
        throw IllegalArgumentException("parent view is not a constraint layout")
    }
    val parentLayout = this.parent as ConstraintLayout
    val constraintSet = ConstraintSet()
    val constraintType = ConstraintType()
    constraintSet.clone(parentLayout)
    block.invoke(constraintType)
    val id = this@setConstraints.id
    constraintType.run {
        topToTopOf?.let {
            constraintSet.connect(id, ConstraintSet.TOP, it, ConstraintSet.TOP)
        }
        topToBottomOf?.let {
            constraintSet.connect(
                id,
                ConstraintSet.TOP,
                it,
                ConstraintSet.BOTTOM
            )
        }
        topToLeftOf?.let {
            constraintSet.connect(
                id,
                ConstraintSet.TOP,
                it,
                ConstraintSet.LEFT
            )
        }
        topToRightOf?.let {
            constraintSet.connect(
                id,
                ConstraintSet.TOP,
                it,
                ConstraintSet.RIGHT
            )
        }
        startToEndOf?.let {
            constraintSet.connect(
                id,
                ConstraintSet.START,
                it,
                ConstraintSet.END
            )
        }
        bottomToBottomOf?.let {
            constraintSet.connect(
                id,
                ConstraintSet.BOTTOM,
                it,
                ConstraintSet.BOTTOM
            )
        }
        startToStartOf?.let {
            constraintSet.connect(
                id,
                ConstraintSet.START,
                it,
                ConstraintSet.START
            )
        }
        bottomToTopOf?.let {
            constraintSet.connect(
                id,
                ConstraintSet.BOTTOM,
                it,
                ConstraintSet.TOP
            )
        }
        endToEndOf?.let {
            constraintSet.connect(
                id,
                ConstraintSet.END,
                it,
                ConstraintSet.END
            )
        }
    }
    constraintSet.applyTo(parentLayout)
}

inline fun <T> T.applyIf(predicate: T.() -> Boolean, block: T.() -> Unit): T = apply {
    if (predicate(this))
        block(this)
}

fun String.getQueryParams(): Map<String, String?> {
    val uri = Uri.parse(this)
    val queryParameters = uri.queryParameterNames.associateWith { params ->
        uri.getQueryParameter(params)
    }
    return queryParameters
}


fun String.getQueryParamValue(queryParam: String): String? {
    val uri = Uri.parse(this)
    return uri.getQueryParameter(queryParam)
}


fun View.performHapticFeedback() {
    this.isHapticFeedbackEnabled = true
    this.performHapticFeedback(HapticFeedbackConstants.CONTEXT_CLICK)
}

fun Editable?.validateRegex(regex: String?): Boolean {
    return if (regex != null) {
        val text = this.toString()
        val pattern: Pattern = Pattern.compile(regex)
        return pattern.matcher(text).matches()
    } else {
        true
    }
}

fun Int.isEven() = this % 2 == 0