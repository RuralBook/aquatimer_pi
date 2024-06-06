package com.tobiask.model

import kotlinx.serialization.Serializable

@Serializable
data class Measurement(
    val time: String,
    val measurement: Double
){
    override fun toString(): String {
        val returnString = "${this.time}@${this.measurement}"
        return returnString
    }
}
