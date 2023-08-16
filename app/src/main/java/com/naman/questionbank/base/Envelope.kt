package com.naman.questionbank.base

data class Envelope(
    val action : ActionType? = null,
    val data : Any? = null
)

enum class ActionType{
    OPEN_OPTION_FRAGMENT,OPEN_PDF_ACTIVITY,OPEN_PDF_VIEWER_FRAGMENT,CLOSE_PROGRESS_BAR,OPEN_PAYMENT_ACTIVITY
}
