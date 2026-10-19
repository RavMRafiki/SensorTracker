package com.ravmrafiki.sensortracker.presentation.settings

import androidx.lifecycle.ViewModel
import com.ravmrafiki.sensortracker.data.repository.SettingsRepository
import com.ravmrafiki.sensortracker.data.sensor.SensorConfig
import com.ravmrafiki.sensortracker.data.sensor.SensorSamplingDelay
import com.ravmrafiki.sensortracker.data.sensor.SensorType
import kotlinx.coroutines.flow.StateFlow

class SettingsViewModel(
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    val sensorConfigs: StateFlow<List<SensorConfig>> = settingsRepository.sensorConfigs

    fun onEnabledChange(type: SensorType, isEnabled: Boolean) {
        settingsRepository.updateEnabled(type, isEnabled)
    }

    fun onDelayChange(type: SensorType, delay: SensorSamplingDelay) {
        settingsRepository.updateDelay(type, delay)
    }
}
