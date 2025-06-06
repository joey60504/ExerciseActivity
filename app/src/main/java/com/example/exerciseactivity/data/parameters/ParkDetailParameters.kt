package com.example.exerciseactivity.data.parameters

import com.example.exerciseactivity.data.response.Park

data class ParkDetailParameters(
    val scope: String,
    var limit: Int,
    var offset: Int,
    var showStart: Int,
    var park: Park?
)