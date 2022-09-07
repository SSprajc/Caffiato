package hr.algebra.caffiato.models

import java.time.LocalDateTime

data class Transaction(
    val amount: Int,
    val datetime: LocalDateTime,
)
