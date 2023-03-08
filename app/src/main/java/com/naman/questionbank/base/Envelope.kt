package com.naman.questionbank.base

data class Envelope(
    val action : ActionType? = null,
    val data : Any? = null
)

enum class ActionType{
    OPEN_OPTION_FRAGMENT
}
