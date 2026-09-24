package com.example.sensortracker

import android.content.Context
import android.os.Environment
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.sensortracker.sensor.MeasurableSensor
import com.example.sensortracker.sensor.SensorData
import com.example.sensortracker.sensor.SensorRecord
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.lifecycle.viewModelScope

class MainViewModel(
    private val accelerometer: MeasurableSensor,
    private val gyroscope: MeasurableSensor,
): ViewModel() {

    var accelerometerData by mutableStateOf(SensorData())
        private set

    var gyroscopeData by mutableStateOf(SensorData())
        private set

    var isRecording by mutableStateOf(false)
        private set

    private val recordedData = mutableListOf<SensorRecord>()

    private val sensorChannel = Channel<SensorRecord>(
        capacity = Channel.UNLIMITED,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    init {
        viewModelScope.launch(Dispatchers.Default) {
            for (record in sensorChannel) {
                recordedData.add(record)
            }
        }

        accelerometer.setOnSensorValuesChangedListener { values ->
            val data = SensorData(x = values[0], y = values[1], z = values[2])
            val timestampVal = System.nanoTime()
            accelerometerData = data
            if (isRecording) {
                sensorChannel.trySend(
                    SensorRecord(timestampVal, "Accelerometer", data.x, data.y, data.z)
                )
            }
        }
        accelerometer.startListening()

        gyroscope.setOnSensorValuesChangedListener { values ->
            val data = SensorData(x = values[0], y = values[1], z = values[2])
            val timestampVal = System.nanoTime()
            gyroscopeData = data
            if (isRecording) {
                sensorChannel.trySend(
                    SensorRecord(timestampVal, "Gyroscope", data.x, data.y, data.z)
                )
            }
        }
        gyroscope.startListening()
    }

    fun startRecording() {
        isRecording = true
    }

    fun pauseRecording() {
        isRecording = false
    }

    fun saveToCsv(context: Context): String? {
        if (recordedData.isEmpty()) return null
        
        val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val fileName = "sensor_data_$timestamp.csv"
        
        return try {
            val file = File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), fileName)
            FileOutputStream(file).use { fos ->
                fos.write("timestamp,sensor,x,y,z\n".toByteArray())
                recordedData.forEach { record ->
                    val line = "${record.timestamp},${record.sensorType},${record.x},${record.y},${record.z}\n"
                    fos.write(line.toByteArray())
                }
            }
            recordedData.clear()
            file.absolutePath
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    override fun onCleared() {
        super.onCleared()
        accelerometer.stopListening()
        gyroscope.stopListening()
    }
}
