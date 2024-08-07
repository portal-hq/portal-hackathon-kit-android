package io.portalhq.portalhackathon.data

import io.portalhq.portalhackathon.data.apimodels.BuildTransactionApiRequest
import io.portalhq.portalhackathon.data.apimodels.BuildTransactionApiResponse
import io.portalhq.portalhackathon.data.apimodels.GetAssetsApiResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface PortalSolanaApi {
    @GET("assets")
    suspend fun getAssets(): GetAssetsApiResponse

    @POST("assets/send/build-transaction")
    suspend fun buildTransaction(
        @Body request: BuildTransactionApiRequest
    ): BuildTransactionApiResponse
}