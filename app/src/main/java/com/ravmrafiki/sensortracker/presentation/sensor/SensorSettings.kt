package com.ravmrafiki.sensortracker.presentation.sensor

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ravmrafiki.sensortracker.data.sensor.SensorSamplingDelay
import com.ravmrafiki.sensortracker.data.sensor.SensorType
import com.ravmrafiki.sensortracker.ui.theme.CardBorderStroke
import com.ravmrafiki.sensortracker.ui.theme.DeepNavyBlue
import com.ravmrafiki.sensortracker.ui.theme.DropdownMenuSurface
import com.ravmrafiki.sensortracker.ui.theme.ElevatedCardSurface
import com.ravmrafiki.sensortracker.ui.theme.PrimaryText
import com.ravmrafiki.sensortracker.ui.theme.SecondaryText
import com.ravmrafiki.sensortracker.ui.theme.VibrantCyanTeal

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SensorSettingCard(
    sensorType: SensorType,
    isEnabled: Boolean,
    isAvailable: Boolean,
    selectedDelay: SensorSamplingDelay,
    onEnabledChange: (Boolean) -> Unit,
    onDelayChange: (SensorSamplingDelay) -> Unit,
    modifier: Modifier = Modifier
) {
    var dropdownExpanded by remember { mutableStateOf(false) }

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = ElevatedCardSurface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header Row: Toggle & Label
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(enabled = isAvailable) { onEnabledChange(!isEnabled) },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = sensorType.displayName,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = PrimaryText
                    )
                    Text(
                        text = if (!isAvailable) "Unavailable" else if (isEnabled) "Active" else "Disabled",
                        style = MaterialTheme.typography.bodySmall,
                        color = SecondaryText
                    )
                }
                Switch(
                    checked = isEnabled,
                    onCheckedChange = onEnabledChange,
                    enabled = isAvailable,
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = ElevatedCardSurface,
                        checkedTrackColor = VibrantCyanTeal,
                        uncheckedThumbColor = ElevatedCardSurface,
                        uncheckedTrackColor = CardBorderStroke,
                        disabledUncheckedThumbColor = ElevatedCardSurface,
                    )
                )
            }

            // Expandable Delay Selector: Only visible/active when enabled
            AnimatedVisibility(visible = isEnabled) {
                Column(modifier = Modifier.padding(top = 12.dp)) {
                    HorizontalDivider(
                        modifier = Modifier.padding(bottom = 12.dp),
                        color = CardBorderStroke
                    )

                    Text(
                        text = "Sampling Frequency / Delay",
                        style = MaterialTheme.typography.labelMedium,
                        color = SecondaryText
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
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = DropdownMenuSurface,
                                unfocusedContainerColor = DropdownMenuSurface,
                                focusedBorderColor = DeepNavyBlue,
                                unfocusedBorderColor = CardBorderStroke,
                                focusedTextColor = PrimaryText,
                                unfocusedTextColor = PrimaryText
                            ),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryEditable)
                                .fillMaxWidth()
                        )

                        ExposedDropdownMenu(
                            expanded = dropdownExpanded,
                            onDismissRequest = { dropdownExpanded = false }
                        ) {
                            SensorSamplingDelay.entries.forEach { delayOption ->
                                DropdownMenuItem(
                                    text = { Text(delayOption.label, color = PrimaryText) },
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
