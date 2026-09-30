package com.example.sensortracker.presentation.sensor

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.sensortracker.data.sensor.SensorSamplingDelay
import com.example.sensortracker.data.sensor.SensorType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SensorSettingCard(
    sensorType: SensorType,
    isEnabled: Boolean,
    selectedDelay: SensorSamplingDelay,
    onEnabledChange: (Boolean) -> Unit,
    onDelayChange: (SensorSamplingDelay) -> Unit,
    modifier: Modifier = Modifier
) {
    var dropdownExpanded by remember { mutableStateOf(false) }

    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header Row: Toggle & Label
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onEnabledChange(!isEnabled) },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = sensorType.displayName,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = if (isEnabled) "Active" else "Disabled",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Switch(
                    checked = isEnabled,
                    onCheckedChange = onEnabledChange
                )
            }

            // Expandable Delay Selector: Only visible/active when enabled
            AnimatedVisibility(visible = isEnabled) {
                Column(modifier = Modifier.padding(top = 12.dp)) {
                    Divider(modifier = Modifier.padding(bottom = 12.dp))

                    Text(
                        text = "Sampling Frequency / Delay",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(4.dp))

                    ExposedDropdownMenuBox(
                        expanded = dropdownExpanded,
                        onExpandedChange = { dropdownExpanded = !dropdownExpanded }
                    ) {
                        OutlinedTextField(
                            value = selectedDelay.label,
                            onValueChange = {},
                            readOnly = true,
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = dropdownExpanded) },
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth()
                        )

                        ExposedDropdownMenu(
                            expanded = dropdownExpanded,
                            onDismissRequest = { dropdownExpanded = false }
                        ) {
                            SensorSamplingDelay.values().forEach { delayOption ->
                                DropdownMenuItem(
                                    text = { Text(delayOption.label) },
                                    onClick = {
                                        onDelayChange(delayOption)
                                        dropdownExpanded = false
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}