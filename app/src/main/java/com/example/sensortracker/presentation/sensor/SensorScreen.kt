package com.example.sensortracker.presentation.sensor

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.DirectionsWalk
import androidx.compose.material.icons.automirrored.outlined.RotateRight
import androidx.compose.material.icons.automirrored.outlined.TrendingUp
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.outlined.ArrowDownward
import androidx.compose.material.icons.outlined.CompassCalibration
import androidx.compose.material.icons.outlined.Compress
import androidx.compose.material.icons.outlined.Explore
import androidx.compose.material.icons.outlined.FolderOpen
import androidx.compose.material.icons.outlined.ScreenRotation
import androidx.compose.material.icons.outlined.Sensors
import androidx.compose.material.icons.outlined.Speed
import androidx.compose.material.icons.outlined.Thermostat
import androidx.compose.material.icons.outlined.WaterDrop
import androidx.compose.material.icons.outlined.WbSunny
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.sensortracker.data.model.SensorData
import com.example.sensortracker.data.sensor.SensorType
import com.example.sensortracker.ui.theme.CardBorderStroke
import com.example.sensortracker.ui.theme.DeepNavyBlue
import com.example.sensortracker.ui.theme.DisabledButtonBackground
import com.example.sensortracker.ui.theme.DisabledTextAndIcon
import com.example.sensortracker.ui.theme.ElectricAzureBlue
import com.example.sensortracker.ui.theme.ElevatedCardSurface
import com.example.sensortracker.ui.theme.PrimaryText
import com.example.sensortracker.ui.theme.RichCobaltNavy
import com.example.sensortracker.ui.theme.SecondaryText
import com.example.sensortracker.ui.theme.VibrantCyanTeal

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
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        // Top Header
        Text(
            text = "Sensor Tracker",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            color = DeepNavyBlue,
            fontSize = 32.sp,
            modifier = Modifier.padding(bottom = 16.dp, top = 8.dp)
        )

        // Sensor Cards List
        if (uiState.activeSensorData.isEmpty()) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No sensors active. Enable sensors in Settings.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = SecondaryText
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(
                    items = uiState.activeSensorData.toList(),
                    key = { (sensorType, _) -> sensorType.name }
                ) { (sensorType, data) ->
                    SensorCard(sensorType = sensorType, data = data)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Recording Status Indicator Pill
        RecordingStatusPill(
            isRecording = uiState.isRecording,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 2x2 Action Buttons
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Row 1: Start & Pause
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = { viewModel.startRecording() },
                    enabled = !uiState.isRecording,
                    shape = CircleShape,
                    modifier = Modifier
                        .weight(1f)
                        .height(52.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = DeepNavyBlue,
                        contentColor = Color.White,
                        disabledContainerColor = DisabledButtonBackground,
                        disabledContentColor = DisabledTextAndIcon
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "Start",
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Start",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Button(
                    onClick = { viewModel.pauseRecording() },
                    enabled = uiState.isRecording,
                    shape = CircleShape,
                    modifier = Modifier
                        .weight(1f)
                        .height(52.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = DeepNavyBlue,
                        contentColor = Color.White,
                        disabledContainerColor = DisabledButtonBackground,
                        disabledContentColor = DisabledTextAndIcon
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Pause,
                        contentDescription = "Pause",
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Pause",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            // Row 2: Save CSV & Open CSV
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = { viewModel.saveToCsv() },
                    shape = CircleShape,
                    modifier = Modifier
                        .weight(1f)
                        .height(52.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = DeepNavyBlue,
                        contentColor = Color.White
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Save,
                        contentDescription = "Save CSV",
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Save CSV",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                OutlinedButton(
                    onClick = { viewModel.openFiles(context) },
                    shape = CircleShape,
                    border = BorderStroke(1.5.dp, DeepNavyBlue),
                    modifier = Modifier
                        .weight(1f)
                        .height(52.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = ElevatedCardSurface,
                        contentColor = DeepNavyBlue
                    )
                ) {
                    Icon(
                        imageVector = Icons.Outlined.FolderOpen,
                        contentDescription = "Open CSV",
                        modifier = Modifier.size(20.dp),
                        tint = DeepNavyBlue
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Open CSV",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

@Composable
fun RecordingStatusPill(
    isRecording: Boolean,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = CircleShape,
        color = CardBorderStroke,
        border = BorderStroke(1.dp, DisabledButtonBackground)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 18.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .background(
                        color = if (isRecording) Color(0xFFE53935) else DisabledTextAndIcon,
                        shape = CircleShape
                    )
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = if (isRecording) "RECORDING" else "NOT RECORDING",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = SecondaryText,
                letterSpacing = 0.5.sp
            )
        }
    }
}

@Composable
fun SensorCard(
    sensorType: SensorType,
    data: SensorData,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = ElevatedCardSurface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 18.dp)
        ) {
            // Sensor Icon & Title
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = getSensorIcon(sensorType),
                    contentDescription = sensorType.displayName,
                    tint = ElectricAzureBlue,
                    modifier = Modifier.size(30.dp)
                )
                Spacer(modifier = Modifier.width(14.dp))
                Text(
                    text = sensorType.displayName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = PrimaryText
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Values Row / Badges
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (isMultiAxisSensor(sensorType)) {
                    SensorValuePill(
                        label = "X: ${"%.2f".format(data.x).replace('-', '\u2212').padStart(7, '\u2007')}",
                        backgroundColor = VibrantCyanTeal,
                    )
                    SensorValuePill(
                        label = "Y: ${"%.2f".format(data.y).replace('-', '\u2212').padStart(7, '\u2007')}",
                        backgroundColor = ElectricAzureBlue
                    )
                    SensorValuePill(
                        label = "Z: ${"%.2f".format(data.z).replace('-', '\u2212').padStart(7, '\u2007')}",
                        backgroundColor = RichCobaltNavy
                    )
                } else {
                    SensorValuePill(
                        label = "Value: ${"%.2f".format(data.x).replace('-', '\u2212').padStart(7, '\u2007')}",
                        backgroundColor = VibrantCyanTeal
                    )
                }
            }
        }
    }
}

@Composable
fun SensorValuePill(
    label: String,
    backgroundColor: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = CircleShape,
        color = backgroundColor
    ) {
        Text(
            text = label,
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            fontSize = 14.sp,
            fontFamily = FontFamily.Monospace,
        )
    }
}

fun isMultiAxisSensor(type: SensorType): Boolean {
    return when (type) {
        SensorType.ACCELEROMETER,
        SensorType.GYROSCOPE,
        SensorType.LINEAR_ACCELERATION,
        SensorType.GRAVITY,
        SensorType.GEOMAGNETIC_ROTATION_VECTOR,
        SensorType.MAGNETIC_FIELD,
        SensorType.ROTATION_VECTOR -> true
        else -> false
    }
}

fun getSensorIcon(type: SensorType): ImageVector {
    return when (type) {
        SensorType.ACCELEROMETER -> Icons.Outlined.Speed
        SensorType.GYROSCOPE -> Icons.Outlined.Explore
        SensorType.LINEAR_ACCELERATION -> Icons.AutoMirrored.Outlined.TrendingUp
        SensorType.GRAVITY -> Icons.Outlined.ArrowDownward
        SensorType.GEOMAGNETIC_ROTATION_VECTOR -> Icons.AutoMirrored.Outlined.RotateRight
        SensorType.STEP_DETECTOR -> Icons.AutoMirrored.Outlined.DirectionsWalk
        SensorType.MAGNETIC_FIELD -> Icons.Outlined.CompassCalibration
        SensorType.ROTATION_VECTOR -> Icons.Outlined.ScreenRotation
        SensorType.PROXIMITY -> Icons.Outlined.Sensors
        SensorType.LIGHT -> Icons.Outlined.WbSunny
        SensorType.PRESSURE -> Icons.Outlined.Compress
        SensorType.AMBIENT_TEMPERATURE -> Icons.Outlined.Thermostat
        SensorType.RELATIVE_HUMIDITY -> Icons.Outlined.WaterDrop
    }
}
