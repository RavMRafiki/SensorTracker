package com.ravmrafiki.sensortracker.presentation.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ravmrafiki.sensortracker.presentation.sensor.SensorSettingCard
import com.ravmrafiki.sensortracker.ui.theme.DeepNavyBlue
import com.ravmrafiki.sensortracker.ui.theme.SoftIceBlue

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel,
    modifier: Modifier = Modifier
) {
    val settingsList by viewModel.sensorConfigs.collectAsStateWithLifecycle()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(SoftIceBlue)
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        Text(
            text = "Settings",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            color = DeepNavyBlue,
            fontSize = 32.sp,
            modifier = Modifier.padding(bottom = 16.dp, top = 8.dp)
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(
                items = settingsList,
                key = { it.type.name }
            ) { config ->
                SensorSettingCard(
                    sensorType = config.type,
                    isEnabled = config.isEnabled,
                    isAvailable = config.isAvailable,
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
}
