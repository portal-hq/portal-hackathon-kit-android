package io.portalhq.portalhackathon.data

import retrofit2.http.GET

interface PortalSolanaApi {
    @GET("assets/")
    suspend fun getAssets(): List<Any>
}