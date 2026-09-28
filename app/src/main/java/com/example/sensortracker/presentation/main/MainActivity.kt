package com.example.sensortracker.presentation.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.sensortracker.data.repository.CsvSensorStorageRepository
import com.example.sensortracker.data.sensor.AccelerometerSensor
import com.example.sensortracker.data.sensor.GyroscopeSensor
import com.example.sensortracker.data.sensor.LinearAccelerometerSensor
import com.example.sensortracker.presentation.sensor.SensorScreen
import com.example.sensortracker.presentation.sensor.SensorViewModel
import com.example.sensortracker.ui.theme.SensorTrackerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SensorTrackerTheme {
                val viewModel = viewModel<SensorViewModel>(
                    factory = object : ViewModelProvider.Factory {
                        @Suppress("UNCHECKED_CAST")
                        override fun <T : ViewModel> create(modelClass: Class<T>): T {
                            return SensorViewModel(
                                accelerometer = AccelerometerSensor(applicationContext),
                                gyroscope = GyroscopeSensor(applicationContext),
                                linearAcceleration = LinearAccelerometerSensor(applicationContext),
                                storageRepository = CsvSensorStorageRepository(applicationContext)
                            ) as T
                        }
                    }
                )
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    SensorScreen(
                        viewModel = viewModel,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}
