package com.example.exerciseactivity.ui.parkdetail

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.example.exerciseactivity.data.GetParkDataInfoUseCase
import com.example.exerciseactivity.data.parameters.ParkDetailParameters
import com.example.exerciseactivity.data.response.Park
import com.example.exerciseactivity.data.response.ParkInfo

class ParkDetailListDataSourceFactory  (
    private val getParkDataInfoUseCase: GetParkDataInfoUseCase,
    private val parameter: ParkDetailParameters,
    private val onHeaderLoaded: (Park) -> Unit
) : DataSource.Factory<Int, ParkInfo>(){
    val sourceLiveData = MutableLiveData<ParkDetailListDataSource>()
    override fun create(): DataSource<Int, ParkInfo> {
        val source = ParkDetailListDataSource(
            parameter,
            getParkDataInfoUseCase,
            onHeaderLoaded
        )
        sourceLiveData.postValue(source)
        return source
    }
}