package hr.algebra.caffiato.api

import com.google.gson.annotations.SerializedName

data class FeedbackItem(
    @SerializedName("idFeedback") val idFeedback : Int?,
    @SerializedName("feedbackLog") val feedbackLog : String
)
