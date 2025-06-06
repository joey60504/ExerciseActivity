package com.example.exerciseactivity.data

import com.example.exerciseactivity.data.parameters.ParkDetailParameters
import com.example.exerciseactivity.data.response.ParkResultInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetParkDataInfoUseCase @Inject constructor(
    private val service: ParkService
) {
    operator fun invoke(parameters: ParkDetailParameters): Flow<ParkResultInfo> = flow {
        val response = service.getParksInfo(
            scope = parameters.scope,
            limit = parameters.limit,
            offset = parameters.offset
        )
        emit(response.result)
    }.catch { e ->
        e.printStackTrace()
        emit(ParkResultInfo(null, null, null, null, listOf()))
    }
}
