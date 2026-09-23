package com.example.sensortracker

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.sensortracker.sensor.AccelerometerSensor
import com.example.sensortracker.sensor.GyroscopeSensor
import com.example.sensortracker.sensor.SensorData
import com.example.sensortracker.ui.theme.SensorTrackerTheme
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SensorTrackerTheme {
                val viewModel = viewModel<MainViewModel>(
                    factory = object : ViewModelProvider.Factory {
                        override fun <T : ViewModel> create(modelClass: Class<T>): T {
                            return MainViewModel(
                                accelerometer = AccelerometerSensor(applicationContext),
                                gyroscope = GyroscopeSensor(applicationContext)
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

@Composable
fun SensorScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    
    Column(
        modifier = modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        SensorDisplay(title = "Accelerometer", data = viewModel.accelerometerData)
        Spacer(modifier = Modifier.height(32.dp))
        SensorDisplay(title = "Gyroscope", data = viewModel.gyroscopeData)
        
        Spacer(modifier = Modifier.height(48.dp))
        
        Text(
            text = if (viewModel.isRecording) "Recording..." else "Not Recording",
            style = MaterialTheme.typography.bodyLarge,
            color = if (viewModel.isRecording) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = { viewModel.startRecording() },
                enabled = !viewModel.isRecording
            ) {
                Text("Start")
            }
            
            Button(
                onClick = { viewModel.pauseRecording() },
                enabled = viewModel.isRecording
            ) {
                Text("Pause")
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Button(
            onClick = { 
                val path = viewModel.saveToCsv(context)
                if (path != null) {
                    Toast.makeText(context, "Saved to $path", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(context, "Nothing to save", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save CSV")
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
