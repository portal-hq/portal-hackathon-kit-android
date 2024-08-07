package io.portalhq.portalhackathon.data

import io.portalhq.android.Portal
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class OkhttpAuthorizationInterceptor @Inject constructor(
    private val portal: Portal
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val newRequest = originalRequest.newBuilder()
            .header("Authorization", "Bearer ${portal.apiKey}")
            .build()
        return chain.proceed(newRequest)
    }
}