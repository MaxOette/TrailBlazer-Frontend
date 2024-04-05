package de.max.trailblazerfrontendv1.Api

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path

interface HealthService {
    @GET("/dev/health")
    suspend fun getHealth(): HealthResponse
}

object HealthApi {
    private val retrofit = Retrofit.Builder()
        .baseUrl("http://195.201.42.22:8080/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val healthService:HealthService = retrofit.create(HealthService::class.java)
}