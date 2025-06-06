package com.example.exerciseactivity.data

import com.example.exerciseactivity.data.response.ParkResponse
import com.example.exerciseactivity.data.response.ParkResponseInfo
import retrofit2.http.GET
import retrofit2.http.Query

interface ParkService {
    @GET("api/v1/dataset/5a0e5fbb-72f8-41c6-908e-2fb25eff9b8a")
    suspend fun getParks(
        @Query("scope") scope: String
    ): ParkResponse

    @GET("api/v1/dataset/a3e2b221-75e0-45c1-8f97-75acbd43d613")
    suspend fun getParksInfo(
        @Query("scope") scope: String,
        @Query("limit") limit: Int,
        @Query("offset") offset: Int
    ): ParkResponseInfo
}
