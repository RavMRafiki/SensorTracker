package com.example.sensortracker.presentation.graphs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sensortracker.ui.theme.DeepNavyBlue
import com.example.sensortracker.ui.theme.SecondaryText
import com.example.sensortracker.ui.theme.SoftIceBlue

@Composable
fun GraphsScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(SoftIceBlue)
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        Text(
            text = "Graphs",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            color = DeepNavyBlue,
            fontSize = 32.sp,
            modifier = Modifier.padding(bottom = 16.dp, top = 8.dp)
        )

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Sensor Graphs",
                style = MaterialTheme.typography.titleMedium,
                color = SecondaryText
            )
        }
    }
}
