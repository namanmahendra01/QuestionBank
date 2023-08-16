package com.naman.questionbank.payment

import com.google.gson.annotations.SerializedName

data class UserDetailsModel(
    @SerializedName("uid")
    val uid : String? = null,
    @SerializedName("isSubscribed")
    var isSubscribed : Boolean? = false,
    @SerializedName("subscribedDate")
    val subscribedDate : String? = null,
    @SerializedName("subscribedEndDate")
    val subscribedEndDate : String? = null,
    @SerializedName("trialCount")
    var trialCount : Int = 0,
)