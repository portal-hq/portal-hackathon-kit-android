package io.portalhq.portalhackathon.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.portalhq.android.Portal
import io.portalhq.android.storage.mobile.Keychain
import io.portalhq.portalhackathon.core.commonconstants.BlockChainConstants.ALCHEMY_API_KEY
import io.portalhq.portalhackathon.core.commonconstants.BlockChainConstants.SOLANA_DEV_NET_CHAIN_ID
import io.portalhq.portalhackathon.core.commonconstants.BlockChainConstants.SOLANA_MAIN_NET_CHAIN_ID
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PortalModule {
    @Provides
    @Singleton
    fun providePortal(): Portal {
        return Portal(
            apiKey = "YOUR_PORTAL_CLIENT_API_KEY",
            rpcConfig = mapOf(
                SOLANA_MAIN_NET_CHAIN_ID to "https://solana-mainnet.g.alchemy.com/v2/$ALCHEMY_API_KEY",
                SOLANA_DEV_NET_CHAIN_ID to "https://solana-devnet.g.alchemy.com/v2/$ALCHEMY_API_KEY",
            ),
            legacyEthChainId = 0, // this is for legacy code and no longer relevant in this new Portal SDK
            keychain = Keychain()
        )
    }

}