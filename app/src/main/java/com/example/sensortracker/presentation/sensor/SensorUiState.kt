package com.example.sensortracker.presentation.sensor

import com.example.sensortracker.data.model.SensorData

data class SensorUiState(
    val accelerometerData: SensorData = SensorData(),
    val gyroscopeData: SensorData = SensorData(),
    val linearAccelerationData: SensorData = SensorData(),
    val isRecording: Boolean = false,
    val userMessage: String? = null
)
