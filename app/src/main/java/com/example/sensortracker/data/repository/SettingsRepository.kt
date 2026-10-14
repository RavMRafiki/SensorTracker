package com.example.sensortracker.data.repository

import com.example.sensortracker.data.sensor.SensorConfig
import com.example.sensortracker.data.sensor.SensorSamplingDelay
import com.example.sensortracker.data.sensor.SensorType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

interface SettingsRepository {
    val sensorConfigs: StateFlow<List<SensorConfig>>
    fun updateEnabled(type: SensorType, isEnabled: Boolean)
    fun updateDelay(type: SensorType, delay: SensorSamplingDelay)
}

class InMemorySettingsRepository : SettingsRepository {

    private val _sensorConfigs = MutableStateFlow(
        listOf(
            SensorConfig(SensorType.ACCELEROMETER, isEnabled = true, delay = SensorSamplingDelay.GAME),
            SensorConfig(SensorType.GYROSCOPE, isEnabled = true, delay = SensorSamplingDelay.GAME),
            SensorConfig(SensorType.LINEAR_ACCELERATION, isEnabled = true, delay = SensorSamplingDelay.GAME),
            SensorConfig(SensorType.GRAVITY, isEnabled = false, delay = SensorSamplingDelay.GAME),
            SensorConfig(SensorType.GEOMAGNETIC_ROTATION_VECTOR, isEnabled = false, delay = SensorSamplingDelay.GAME),
            SensorConfig(SensorType.STEP_DETECTOR, isEnabled = false, delay = SensorSamplingDelay.GAME),
            SensorConfig(SensorType.MAGNETIC_FIELD, isEnabled = false, delay = SensorSamplingDelay.GAME),
            SensorConfig(SensorType.ROTATION_VECTOR, isEnabled = false, delay = SensorSamplingDelay.GAME),
            SensorConfig(SensorType.PROXIMITY, isEnabled = false, delay = SensorSamplingDelay.GAME),
            SensorConfig(SensorType.LIGHT, isEnabled = false, delay = SensorSamplingDelay.GAME),
            SensorConfig(SensorType.PRESSURE, isEnabled = false, delay = SensorSamplingDelay.GAME),
            SensorConfig(SensorType.AMBIENT_TEMPERATURE, isEnabled = false, delay = SensorSamplingDelay.GAME),
            SensorConfig(SensorType.RELATIVE_HUMIDITY, isEnabled = false, delay = SensorSamplingDelay.GAME)
        )
    )

    override val sensorConfigs: StateFlow<List<SensorConfig>> = _sensorConfigs.asStateFlow()

    override fun updateEnabled(type: SensorType, isEnabled: Boolean) {
        _sensorConfigs.update { current ->
            current.map { config ->
                if (config.type == type) config.copy(isEnabled = isEnabled) else config
            }
        }
    }

    override fun updateDelay(type: SensorType, delay: SensorSamplingDelay) {
        _sensorConfigs.update { current ->
            current.map { config ->
                if (config.type == type) config.copy(delay = delay) else config
            }
        }
    }
}
