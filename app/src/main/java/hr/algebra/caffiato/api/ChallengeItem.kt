package hr.algebra.caffiato.api

import com.google.gson.annotations.SerializedName

data class ChallengeItem(
    @SerializedName("idChallenge") val idChallenge : Int?,
    @SerializedName("name") val name : String,
    @SerializedName("description") val description : String
)
