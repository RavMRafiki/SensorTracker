package com.example.sensortracker.data.model

data class SensorRecord(
    val timestamp: Long,
    val sensorType: String,
    val x: Float,
    val y: Float,
    val z: Float
)
