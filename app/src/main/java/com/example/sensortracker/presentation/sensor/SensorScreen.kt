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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (uiState.activeSensorData.isEmpty()) {
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "No sensors active. Enable sensors in Settings.",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.weight(1f))
        } else {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                items(
                    items = uiState.activeSensorData.toList(),
                    key = { (sensorType, _) -> sensorType.name }
                ) { (sensorType, data) ->
                    SensorDisplay(title = sensorType.displayName, data = data)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

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
                onClick = { viewModel.openFiles(context) }
            ) {
                Text("Open CSV")
            }
        }
    }
}

@Composable
fun SensorDisplay(title: String, data: SensorData) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = title, style = MaterialTheme.typography.titleMedium)
        Text(text = "X: ${"%.2f".format(data.x)} | Y: ${"%.2f".format(data.y)} | Z: ${"%.2f".format(data.z)}")
    }
}
