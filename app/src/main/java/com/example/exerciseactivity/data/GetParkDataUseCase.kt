package com.example.exerciseactivity.data

import com.example.exerciseactivity.data.response.ParkResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetParkDataUseCase @Inject constructor(
    private val service: ParkService
) {
    operator fun invoke(): Flow<ParkResult> = flow {
        val response = service.getParks("resourceAquire")
        emit(response.result)
    }.catch { e ->
        e.printStackTrace()
        emit(ParkResult(null, null, null, null, listOf()))
    }
}
