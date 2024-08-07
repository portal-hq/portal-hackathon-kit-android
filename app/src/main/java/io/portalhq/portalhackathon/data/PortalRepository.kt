package io.portalhq.portalhackathon.data

import io.portalhq.android.Portal
import io.portalhq.android.storage.mobile.PortalNamespace
import io.portalhq.portalhackathon.data.datamodels.UserBalance
import javax.inject.Inject

class PortalRepository @Inject constructor(
    private val portal: Portal,
    private val portalSolanaApi: PortalSolanaApi
) {
    suspend fun createWallet() {
        if (!portal.isWalletOnDevice()) {
            portal.createWallet()
        }
    }

    suspend fun isWalletCreated() = portal.isWalletOnDevice()

    suspend fun getWalletAddress(): String? {
        return if (isWalletCreated()) {
            portal.getAddress(PortalNamespace.SOLANA)
        } else {
            null
        }
    }

    suspend fun getWalletBalance(): UserBalance {
        val assets = portalSolanaApi.getAssets()
        return UserBalance(
            solanaBalance = assets.nativeBalance.balance,
            pyUsdBalance = assets.pyUsdBalance
        )
    }
}