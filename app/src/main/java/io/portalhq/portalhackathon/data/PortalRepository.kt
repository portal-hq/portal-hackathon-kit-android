package io.portalhq.portalhackathon.data

import io.portalhq.android.Portal
import io.portalhq.android.storage.mobile.PortalNamespace
import javax.inject.Inject

class PortalRepository @Inject constructor(private val portal: Portal) {
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
}