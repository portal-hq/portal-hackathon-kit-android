package io.portalhq.portalhackathon.data

import io.portalhq.portalhackathon.data.apimodels.GetAssetsApiResponse
import retrofit2.http.GET

interface PortalSolanaApi {
    @GET("assets/")
    suspend fun getAssets(): GetAssetsApiResponse
}