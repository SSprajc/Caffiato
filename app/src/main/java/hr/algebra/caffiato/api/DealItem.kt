package hr.algebra.caffiato.api

import com.google.gson.annotations.SerializedName

data class DealItem(
    @SerializedName("idDeal") val idDeal : Int,
    @SerializedName("name") val name : String,
    @SerializedName("dateTime") val dateTime : String,
    @SerializedName("points") val points : Int,
    @SerializedName("price") val price : Int,
    @SerializedName("caffeID") val caffeID : Int,
    @SerializedName("active") val active : Boolean
)
