package com.example.sensortracker.data.sensor

data class SensorConfig(
    val type: SensorType,
    val isEnabled: Boolean = false,
    val delay: SensorSamplingDelay = SensorSamplingDelay.NORMAL
)