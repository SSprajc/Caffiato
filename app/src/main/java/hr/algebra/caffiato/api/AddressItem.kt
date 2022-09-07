package hr.algebra.caffiato.api

import com.google.gson.annotations.SerializedName

data class AddressItem(
    @SerializedName("idAdress") val idAdress : Int,
    @SerializedName("streetNumber") val streetNumber : Int,
    @SerializedName("streetName") val streetName : String,
    @SerializedName("city") val city : String,
    @SerializedName("postCode") val postCode : Int,
    @SerializedName("caffeID") val caffeID : Int
)
