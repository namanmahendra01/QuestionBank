package com.naman.questionbank.ui.snippetData

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ExamCategoryCardData(
  @Expose @SerializedName("image_url") val imgUrl : String? = null,
  @Expose @SerializedName("title") val title : String? = null
)
