package com.naman.questionbank.ui.snippetData

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class SpinnerData(
    @Expose @SerializedName("items") val items : List<String>? = null,
    @Expose @SerializedName("title") val title : String? = null
)
