package com.naman.questionbank.ui.snippetData

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class PdfViewerData(
    @Expose @SerializedName("url") val url : String? = null
)