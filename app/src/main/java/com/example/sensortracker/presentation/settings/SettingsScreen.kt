package com.example.sensortracker.presentation.settings

import com.example.sensortracker.presentation.sensor.SensorSettingCard
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.sensortracker.data.sensor.SensorSamplingDelay
import com.example.sensortracker.data.sensor.SensorType
import com.example.sensortracker.data.sensor.SensorConfig

@Composable
fun SettingsScreen() {
    // In a real app, this comes from a ViewModel (e.g. StateFlow)
    var settingsList by remember {
        mutableStateOf(
            listOf(
                SensorConfig(SensorType.ACCELEROMETER, isEnabled = true, delay = SensorSamplingDelay.GAME),
                SensorConfig(SensorType.GYROSCOPE, isEnabled = false, delay = SensorSamplingDelay.GAME),
                SensorConfig(SensorType.LINEAR_ACCELERATION, isEnabled = false, delay = SensorSamplingDelay.GAME),
                SensorConfig(SensorType.GRAVITY, isEnabled = false, delay = SensorSamplingDelay.GAME),
                SensorConfig(SensorType.GEOMAGNETIC_ROTATION_VECTOR, isEnabled = false, delay = SensorSamplingDelay.GAME),
                SensorConfig(SensorType.STEP_DETECTOR, isEnabled = false, delay = SensorSamplingDelay.GAME),
                SensorConfig(SensorType.MAGNETIC_FIELD, isEnabled = false, delay = SensorSamplingDelay.GAME),
                SensorConfig(SensorType.ROTATION_VECTOR, isEnabled = false, delay = SensorSamplingDelay.GAME),
                SensorConfig(SensorType.PROXIMITY, isEnabled = false, delay = SensorSamplingDelay.GAME),
                SensorConfig(SensorType.LIGHT, isEnabled = false, delay = SensorSamplingDelay.GAME),
                SensorConfig(SensorType.PRESSURE, isEnabled = false, delay = SensorSamplingDelay.GAME),
                SensorConfig(SensorType.AMBIENT_TEMPERATURE, isEnabled = false, delay = SensorSamplingDelay.GAME),
                SensorConfig(SensorType.RELATIVE_HUMIDITY, isEnabled = false, delay = SensorSamplingDelay.GAME),
            )
        )
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(
            items = settingsList,
            key = { it.type.name }
        ) { config ->
            SensorSettingCard(
                sensorType = config.type,
                isEnabled = config.isEnabled,
                selectedDelay = config.delay,
                onEnabledChange = { newEnabled ->
                    settingsList = settingsList.map {
                        if (it.type == config.type) it.copy(isEnabled = newEnabled) else it
                    }
                },
                onDelayChange = { newDelay ->
                    settingsList = settingsList.map {
                        if (it.type == config.type) it.copy(delay = newDelay) else it
                    }
                }
            )
        }
    }
}