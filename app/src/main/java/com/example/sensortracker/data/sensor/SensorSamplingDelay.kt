package com.example.sensortracker.data.sensor

import android.hardware.SensorManager

enum class SensorSamplingDelay(val label: String, val delayUs: Int) {
    FASTEST("Fastest (0ms)", SensorManager.SENSOR_DELAY_FASTEST),
    GAME("Game (20ms)", SensorManager.SENSOR_DELAY_GAME),
    UI("UI (66ms)", SensorManager.SENSOR_DELAY_UI),
    NORMAL("Normal (200ms)", SensorManager.SENSOR_DELAY_NORMAL)
}