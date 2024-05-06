package com.tobiask.model

import java.time.LocalTime


data class Measurement(
    val time: LocalTime,
    val measurement: Int
)
