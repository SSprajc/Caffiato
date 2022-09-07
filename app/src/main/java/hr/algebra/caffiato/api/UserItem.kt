package hr.algebra.caffiato.api

import com.google.gson.annotations.SerializedName

data class UserItem(
    @SerializedName("idUserCaffe") val idUserCaffe : Int?,
    @SerializedName("email") val email : String,
    @SerializedName("name") val name : String?,
    @SerializedName("username") var username : String,
    @SerializedName("surname") val surname : String?,
    @SerializedName("dateOfBirth") val dateOfBirth : String?,
    @SerializedName("oib") val oib : String?,
    @SerializedName("points") var points : Int,
    @SerializedName("transactions") val transactions : List<TransactionItem>?,
    @SerializedName("caffes") val caffes : List<String>?
)
