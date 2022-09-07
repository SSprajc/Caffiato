package hr.algebra.caffiato.models

data class User(
    var _id: Int?,
    val email: String,
    var username: String,
    val points: Int
)
