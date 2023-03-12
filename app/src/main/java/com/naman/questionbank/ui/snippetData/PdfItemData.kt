package com.naman.questionbank.ui.snippetData

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class PdfItemData(
    @Expose @SerializedName("title") val title : String? = null
)
