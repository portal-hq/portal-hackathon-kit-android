package io.portalhq.portalhackathon.data.apimodels

data class BuildTransactionApiResponse(
    val transaction: String
)

data class Transaction(
    val from: String,
    val to: String,
    val data: String
)