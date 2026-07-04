package com.example.sensortracker.sensor

data class SensorRecord(
    val timestamp: Long,
    val sensorType: String,
    val x: Float,
    val y: Float,
    val z: Float
)
