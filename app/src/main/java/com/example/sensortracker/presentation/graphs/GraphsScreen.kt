package com.example.sensortracker.presentation.graphs

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.sensortracker.R
import com.example.sensortracker.data.model.SensorData
import com.example.sensortracker.data.sensor.SensorType
import com.example.sensortracker.presentation.sensor.SensorValuePill
import com.example.sensortracker.presentation.sensor.SensorViewModel
import com.example.sensortracker.presentation.sensor.getSensorIcon
import com.example.sensortracker.presentation.sensor.isMultiAxisSensor
import com.example.sensortracker.ui.theme.DeepNavyBlue
import com.example.sensortracker.ui.theme.ElectricAzureBlue
import com.example.sensortracker.ui.theme.ElevatedCardSurface
import com.example.sensortracker.ui.theme.PrimaryText
import com.example.sensortracker.ui.theme.RichCobaltNavy
import com.example.sensortracker.ui.theme.SecondaryText
import com.example.sensortracker.ui.theme.SoftIceBlue
import com.example.sensortracker.ui.theme.VibrantCyanTeal
import kotlin.math.abs

@Composable
fun GraphsScreen(
    viewModel: SensorViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(SoftIceBlue)
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp, top = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Graphs",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = DeepNavyBlue,
                fontSize = 32.sp
            )
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_playstore),
                contentDescription = "App Icon",
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(DeepNavyBlue)
            )
        }

        if (uiState.activeSensorData.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No sensors active. Enable sensors in Settings.",
                    style = MaterialTheme.typography.titleMedium,
                    color = SecondaryText
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(
                    items = uiState.activeSensorData.toList(),
                    key = { (sensorType, _) -> sensorType.name }
                ) { (sensorType, data) ->
                    val history = uiState.activeSensorHistory[sensorType] ?: emptyList()
                    SensorGraphCard(
                        sensorType = sensorType,
                        data = data,
                        history = history
                    )
                }
            }
        }
    }
}

@Composable
fun SensorGraphCard(
    sensorType: SensorType,
    data: SensorData,
    history: List<SensorData>,
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
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
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
                        fontSize = 18.sp,
                        color = PrimaryText,
                        maxLines = 1
                    )
                }
                
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (isMultiAxisSensor(sensorType)) {
                        SensorValuePill(
                            label = "X: ${"%.2f".format(data.x).replace('-', '\u2212').padStart(7, '\u2007')}",
                            backgroundColor = VibrantCyanTeal
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
                            label = "V: ${"%.2f".format(data.x).replace('-', '\u2212').padStart(7, '\u2007')}",
                            backgroundColor = VibrantCyanTeal
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            SensorGraphCanvas(
                history = history,
                isMultiAxis = isMultiAxisSensor(sensorType),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
            )
            
            // X-Axis labels
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                for (i in 0..5) {
                    Text(
                        text = "${i}s",
                        style = MaterialTheme.typography.labelSmall,
                        color = SecondaryText
                    )
                }
            }
        }
    }
}

@Composable
fun SensorGraphCanvas(
    history: List<SensorData>,
    isMultiAxis: Boolean,
    modifier: Modifier = Modifier
) {
    val axisColor = Color.LightGray
    val colorX = VibrantCyanTeal
    val colorY = ElectricAzureBlue
    val colorZ = RichCobaltNavy
    
    Canvas(modifier = modifier) {
        val width = size.width
        val height = size.height
        
        drawRect(
            color = axisColor,
            style = Stroke(width = 1.dp.toPx())
        )
        drawLine(
            color = axisColor,
            start = Offset(0f, height / 2f),
            end = Offset(width, height / 2f),
            strokeWidth = 1.dp.toPx()
        )
        for (i in 1..4) {
            val x = width * (i / 5f)
            drawLine(
                color = axisColor,
                start = Offset(x, 0f),
                end = Offset(x, height),
                strokeWidth = 1.dp.toPx()
            )
        }

        if (history.isEmpty()) return@Canvas

        var maxVal = 1f
        history.forEach { 
            maxVal = maxOf(maxVal, abs(it.x), abs(it.y), abs(it.z))
        }
        maxVal *= 1.2f

        val pathX = Path()
        val pathY = Path()
        val pathZ = Path()
        
        val fiveSecondsInNanos = 5_000_000_000f
        val latestTimestamp = history.last().timestamp
        
        history.forEachIndexed { index, data ->
            val timeOffset = latestTimestamp - data.timestamp
            // Calculate x based on time offset, so 5s ago is x=0, and now is x=width
            val x = width * (1f - (timeOffset / fiveSecondsInNanos))
            val yPosForX = (height / 2f) - ((data.x / maxVal) * (height / 2f))
            val yPosForY = (height / 2f) - ((data.y / maxVal) * (height / 2f))
            val yPosForZ = (height / 2f) - ((data.z / maxVal) * (height / 2f))

            if (index == 0) {
                pathX.moveTo(x, yPosForX)
                pathY.moveTo(x, yPosForY)
                pathZ.moveTo(x, yPosForZ)
            } else {
                pathX.lineTo(x, yPosForX)
                pathY.lineTo(x, yPosForY)
                pathZ.lineTo(x, yPosForZ)
            }
        }
        
        drawPath(pathX, colorX, style = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round))
        if (isMultiAxis) {
            drawPath(pathY, colorY, style = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round))
            drawPath(pathZ, colorZ, style = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round))
        }
    }
}
