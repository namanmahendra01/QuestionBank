package com.naman.questionbank.ui.snippetData

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ExamCategoryCardData(
  @Expose @SerializedName("imageUrl") val imageUrl : String? = null,
  @Expose @SerializedName("title") val title : String? = null,
  @Expose @SerializedName("options") val options : List<SpinnerData>? = null,
  @Expose @SerializedName("id") val id : String? = null
)
