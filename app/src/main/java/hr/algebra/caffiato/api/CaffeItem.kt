package hr.algebra.caffiato.api

import com.google.gson.annotations.SerializedName

data class CaffeItem(
    @SerializedName("idCaffe") val idCaffe : Int,
    @SerializedName("name") val name : String,
    @SerializedName("userCaffeID") val userCaffeID : Int,
    @SerializedName("dealList") val dealList : List<DealItem>,
    @SerializedName("adress") val adress : AddressItem
)
