package hr.algebra.caffiato.models

data class Caffe(
    val _id: Int?,
    val name: String,
    val address: Address,
    val dealList: List<Deal>
)
