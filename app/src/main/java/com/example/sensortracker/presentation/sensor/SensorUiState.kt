package com.example.sensortracker.presentation.sensor

import com.example.sensortracker.data.model.SensorData
import com.example.sensortracker.data.sensor.SensorType

data class SensorUiState(
    val accelerometerData: SensorData = SensorData(),
    val gyroscopeData: SensorData = SensorData(),
    val linearAccelerationData: SensorData = SensorData(),
    val activeSensorData: Map<SensorType, SensorData> = emptyMap(),
    val activeSensorHistory: Map<SensorType, List<SensorData>> = emptyMap(),
    val isRecording: Boolean = false,
    val userMessage: String? = null
)
