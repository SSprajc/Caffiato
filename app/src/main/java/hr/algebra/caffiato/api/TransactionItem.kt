package hr.algebra.caffiato.api

import com.google.gson.annotations.SerializedName

data class TransactionItem(
    @SerializedName("idTransaction") var idTransaction : Int?,
    @SerializedName("time") val time : String,
    @SerializedName("amount") val amount : Int,
    @SerializedName("userCaffeID") val userCaffeID : Int
)
