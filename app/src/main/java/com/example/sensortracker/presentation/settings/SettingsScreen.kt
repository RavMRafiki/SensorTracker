package com.example.sensortracker.presentation.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.sensortracker.presentation.sensor.SensorSettingCard

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel
) {
    val settingsList by viewModel.sensorConfigs.collectAsStateWithLifecycle()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(
            items = settingsList,
            key = { it.type.name }
        ) { config ->
            SensorSettingCard(
                sensorType = config.type,
                isEnabled = config.isEnabled,
                selectedDelay = config.delay,
                onEnabledChange = { newEnabled ->
                    viewModel.onEnabledChange(config.type, newEnabled)
                },
                onDelayChange = { newDelay ->
                    viewModel.onDelayChange(config.type, newDelay)
                }
            )
        }
    }
}
