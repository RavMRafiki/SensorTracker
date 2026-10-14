package com.example.sensortracker.presentation.sensor

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sensortracker.data.model.SensorData
import com.example.sensortracker.data.model.SensorRecord
import com.example.sensortracker.data.repository.SensorStorageRepository
import com.example.sensortracker.data.repository.SettingsRepository
import com.example.sensortracker.data.sensor.MeasurableSensor
import com.example.sensortracker.data.sensor.SensorConfig
import com.example.sensortracker.data.sensor.SensorFactory
import com.example.sensortracker.data.sensor.SensorType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SensorViewModel(
    private val sensorFactory: SensorFactory,
    private val settingsRepository: SettingsRepository,
    private val storageRepository: SensorStorageRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SensorUiState())
    val uiState: StateFlow<SensorUiState> = _uiState.asStateFlow()

    private val recordedData = mutableListOf<SensorRecord>()

    private val sensorChannel = Channel<SensorRecord>(
        capacity = Channel.UNLIMITED,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    private val activeSensorsMap = mutableMapOf<SensorType, MeasurableSensor>()

    init {
        viewModelScope.launch(Dispatchers.Default) {
            for (record in sensorChannel) {
                synchronized(recordedData) {
                    recordedData.add(record)
                }
            }
        }

        viewModelScope.launch {
            settingsRepository.sensorConfigs.collect { configs ->
                rebuildSensors(configs)
            }
        }
    }

    private fun rebuildSensors(configs: List<SensorConfig>) {
        // Stop current listening sensors
        activeSensorsMap.values.forEach { it.stopListening() }
        activeSensorsMap.clear()

        // Create new active sensors using Factory based on settings
        val newSensors = sensorFactory.createActiveSensors(configs)
        activeSensorsMap.putAll(newSensors)

        val initialDataMap = newSensors.keys.associateWith { SensorData() }
        _uiState.update { it.copy(activeSensorData = initialDataMap) }

        // Configure listeners and start listening
        activeSensorsMap.forEach { (type, sensor) ->
            sensor.setOnSensorValuesChangedListener { values, timestamp ->
                val x = values.getOrNull(0) ?: 0f
                val y = values.getOrNull(1) ?: 0f
                val z = values.getOrNull(2) ?: 0f
                val data = SensorData(x = x, y = y, z = z)

                _uiState.update { state ->
                    val updatedMap = state.activeSensorData.toMutableMap()
                    updatedMap[type] = data

                    var accel = state.accelerometerData
                    var gyro = state.gyroscopeData
                    var linear = state.linearAccelerationData

                    when (type) {
                        SensorType.ACCELEROMETER -> accel = data
                        SensorType.GYROSCOPE -> gyro = data
                        SensorType.LINEAR_ACCELERATION -> linear = data
                        else -> Unit
                    }

                    state.copy(
                        activeSensorData = updatedMap,
                        accelerometerData = accel,
                        gyroscopeData = gyro,
                        linearAccelerationData = linear
                    )
                }

                if (_uiState.value.isRecording) {
                    sensorChannel.trySend(
                        SensorRecord(timestamp, type.displayName, x, y, z)
                    )
                }
            }
            sensor.startListening()
        }
    }

    fun startRecording() {
        _uiState.update { it.copy(isRecording = true) }
    }

    fun pauseRecording() {
        _uiState.update { it.copy(isRecording = false) }
    }

    fun saveToCsv() {
        viewModelScope.launch {
            val recordsCopy = synchronized(recordedData) {
                val copy = recordedData.toList()
                recordedData.clear()
                copy
            }

            val result = storageRepository.saveRecordsToCsv(recordsCopy)
            result.onSuccess { path ->
                _uiState.update { it.copy(userMessage = "Saved to $path") }
            }.onFailure { error ->
                _uiState.update { it.copy(userMessage = error.message ?: "Failed to save CSV") }
            }
        }
    }

    fun openFiles(context: Context) {
        viewModelScope.launch {
            storageRepository.openDownloadsFolder(context)
        }
    }

    fun userMessageShown() {
        _uiState.update { it.copy(userMessage = null) }
    }

    override fun onCleared() {
        super.onCleared()
        activeSensorsMap.values.forEach { it.stopListening() }
    }
}
