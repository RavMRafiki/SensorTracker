package com.example.sensortracker.presentation.sensor

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.sensortracker.data.model.SensorData

@Composable
fun SensorScreen(
    viewModel: SensorViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(uiState.userMessage) {
        uiState.userMessage?.let { message ->
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
            viewModel.userMessageShown()
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        SensorDisplay(title = "Accelerometer", data = uiState.accelerometerData)
        Spacer(modifier = Modifier.height(32.dp))
        SensorDisplay(title = "Gyroscope", data = uiState.gyroscopeData)
        Spacer(modifier = Modifier.height(32.dp))
        SensorDisplay(title = "LinearAccelerometer", data = uiState.linearAccelerationData)

        Spacer(modifier = Modifier.height(48.dp))

        Text(
            text = if (uiState.isRecording) "Recording..." else "Not Recording",
            style = MaterialTheme.typography.bodyLarge,
            color = if (uiState.isRecording) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                modifier = Modifier.weight(1f),
                onClick = { viewModel.startRecording() },
                enabled = !uiState.isRecording
            ) {
                Text("Start")
            }

            Button(
                modifier = Modifier.weight(1f),
                onClick = { viewModel.pauseRecording() },
                enabled = uiState.isRecording
            ) {
                Text("Pause")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                modifier = Modifier.weight(1f),
                onClick = { viewModel.saveToCsv() }
            ) {
                Text("Save CSV")
            }
            Button(
                modifier = Modifier.weight(1f),
                onClick = { viewModel.saveToCsv() }
            ) {
                Text("Open CSV")
            }
        }
    }
}

@Composable
fun SensorDisplay(title: String, data: SensorData) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = title, style = MaterialTheme.typography.headlineMedium)
        Text(text = "X: ${"%.2f".format(data.x)}")
        Text(text = "Y: ${"%.2f".format(data.y)}")
        Text(text = "Z: ${"%.2f".format(data.z)}")
    }
}
