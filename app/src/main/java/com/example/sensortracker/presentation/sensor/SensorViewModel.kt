package com.example.sensortracker.presentation.sensor

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sensortracker.data.model.SensorData
import com.example.sensortracker.data.model.SensorRecord
import com.example.sensortracker.data.repository.SensorStorageRepository
import com.example.sensortracker.data.sensor.MeasurableSensor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SensorViewModel(
    private val accelerometer: MeasurableSensor,
    private val gyroscope: MeasurableSensor,
    private val linearAcceleration: MeasurableSensor,
    private val storageRepository: SensorStorageRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SensorUiState())
    val uiState: StateFlow<SensorUiState> = _uiState.asStateFlow()

    private val recordedData = mutableListOf<SensorRecord>()

    private val sensorChannel = Channel<SensorRecord>(
        capacity = Channel.UNLIMITED,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    private val sensors = listOf(accelerometer, gyroscope, linearAcceleration)

    init {
        viewModelScope.launch(Dispatchers.Default) {
            for (record in sensorChannel) {
                synchronized(recordedData) {
                    recordedData.add(record)
                }
            }
        }

        setupSensor(accelerometer, "Accelerometer") { data ->
            _uiState.update { it.copy(accelerometerData = data) }
        }

        setupSensor(gyroscope, "Gyroscope") { data ->
            _uiState.update { it.copy(gyroscopeData = data) }
        }

        setupSensor(linearAcceleration, "LinearAccelerometer") { data ->
            _uiState.update { it.copy(linearAccelerationData = data) }
        }

        sensors.forEach { it.startListening() }
    }

    private fun setupSensor(
        sensor: MeasurableSensor,
        sensorName: String,
        onUiUpdate: (SensorData) -> Unit
    ) {
        sensor.setOnSensorValuesChangedListener { values, timestamp ->
            val data = SensorData(x = values[0], y = values[1], z = values[2])
            onUiUpdate(data)
            if (_uiState.value.isRecording) {
                sensorChannel.trySend(
                    SensorRecord(timestamp, sensorName, data.x, data.y, data.z)
                )
            }
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
        sensors.forEach { it.stopListening() }
    }
}
