package io.portalhq.portalhackathon.data.apimodels

import io.portalhq.portalhackathon.core.commonconstants.BlockChainConstants

data class GetAssetsApiResponse(
    val nativeBalance: NativeBalance,
    val tokenBalances: List<TokenBalance>
) {
    val pyUsdBalance: String
        get() {
            return tokenBalances.firstOrNull {
                it.symbol == BlockChainConstants.PYUSD_TOKEN_SYMBOL
            }?.balance ?: return "0"
        }
}

data class NativeBalance(
    val balance: String
    // Add other properties as needed
)

data class TokenBalance(
    val balance: String,
    val symbol: String
    // Add other properties as needed
)