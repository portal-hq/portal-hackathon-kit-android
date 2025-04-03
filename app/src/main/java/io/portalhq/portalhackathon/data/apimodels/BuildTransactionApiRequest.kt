package io.portalhq.portalhackathon.data.apimodels

import io.portalhq.portalhackathon.core.commonconstants.BlockChainConstants

data class BuildTransactionApiRequest(
    val amount: String,
    val to: String,
    val token: String = BlockChainConstants.PYUSD_TOKEN_SYMBOL
)
