package io.portalhq.portalhackathon.data

import io.portalhq.android.Portal
import io.portalhq.android.mpc.data.BackupConfigs
import io.portalhq.android.mpc.data.BackupMethods
import io.portalhq.android.mpc.data.PasswordStorageConfig
import io.portalhq.android.provider.data.PortalRequestMethod
import io.portalhq.android.storage.mobile.PortalNamespace
import io.portalhq.portalhackathon.core.commonconstants.BlockChainConstants
import io.portalhq.portalhackathon.data.apimodels.BuildTransactionApiRequest
import io.portalhq.portalhackathon.data.datamodels.UserBalance
import javax.inject.Inject

class PortalRepository @Inject constructor(
    private val portal: Portal,
    private val portalSolanaApi: PortalSolanaApi,
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

    suspend fun sendPyUSD(amount: String, recipientAddress: String): String {
        val transaction = portalSolanaApi.buildTransaction(
            BuildTransactionApiRequest(
                amount = amount,
                to = recipientAddress
            )
        )
        return portal.request(
            chainId = BlockChainConstants.SOLANA_DEV_NET_CHAIN_ID,
            method = PortalRequestMethod.sol_signAndSendTransaction,
            params = listOf(transaction.transaction)
        ).result as String
    }

    suspend fun backupWalletWithPassword(password: String = "0000") {
        portal.backupWallet(
            backupMethod = BackupMethods.Password,
            backupConfigs = BackupConfigs(PasswordStorageConfig(password))
        )
    }

    suspend fun recoverWalletWithPassword(password: String = "0000"): String {
        return portal.recoverWallet(
            backupMethod = BackupMethods.Password,
            backupConfigs = BackupConfigs(PasswordStorageConfig(password))
        )
    }
}