package com.example.arize

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Wc
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OnboardingLandingPage(
    initialProfile: UserProfile,
    onComplete: (UserProfile) -> Unit,
    modifier: Modifier = Modifier
) {
    var gender by rememberSaveable { mutableStateOf(initialProfile.gender) }
    var heightCm by rememberSaveable { mutableStateOf(initialProfile.heightCm) }
    var currentWeightKg by rememberSaveable { mutableStateOf(initialProfile.currentWeightKg) }
    var targetWeightKg by rememberSaveable { mutableStateOf(initialProfile.targetWeightKg) }
    val selectedDays = remember { mutableStateListOf(*initialProfile.workoutDays.toTypedArray()) }
    var reminderHour by rememberSaveable { mutableIntStateOf(initialProfile.reminderHour) }
    var reminderMinute by rememberSaveable { mutableIntStateOf(initialProfile.reminderMinute) }
    var showTimePicker by remember { mutableStateOf(false) }
    val timePickerState = rememberTimePickerState(initialHour = reminderHour, initialMinute = reminderMinute)

    val bmiRange = bmiTargetRange(heightCm)
    targetWeightKg = targetWeightKg.coerceIn(bmiRange.first, bmiRange.second)

    if (showTimePicker) {
        TimePickerDialog(
            onDismissRequest = { showTimePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    reminderHour = timePickerState.hour
                    reminderMinute = timePickerState.minute
                    showTimePicker = false
                }) { Text("Confirm") }
            },
            dismissButton = { TextButton(onClick = { showTimePicker = false }) { Text("Cancel") } }
        ) { TimePicker(state = timePickerState) }
    }

    val days = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(AppDark)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LinearProgressIndicator(
            progress = { 1f },
            modifier = Modifier.fillMaxWidth().height(8.dp),
            color = AccentBlue,
            trackColor = Color(0xFF20242E)
        )

        Spacer(modifier = Modifier.height(22.dp))
        Text(
            text = "Set Up Your Fitness Profile",
            style = MaterialTheme.typography.headlineMedium,
            color = Color.White,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Tell us your body metrics and schedule once. You can edit these later from profile.",
            color = MutedText,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(top = 8.dp, bottom = 20.dp),
            textAlign = TextAlign.Center
        )

        OutlinedCard(
            colors = CardDefaults.outlinedCardColors(containerColor = CardDark),
            border = BorderStroke(1.dp, Color(0xFF2D3342))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Wc, contentDescription = null, tint = Color.White)
                    Text("Gender", color = Color.White, style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(start = 8.dp))
                }
                Spacer(modifier = Modifier.height(10.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    GenderType.entries.forEach { g ->
                        FilterChip(selected = gender == g, onClick = { gender = g }, label = { Text(g.label) })
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                ProfileSliderRow("Height: ${heightCm.toInt()} cm", heightCm, 130f..220f) { heightCm = it }
                Spacer(modifier = Modifier.height(12.dp))
                ProfileSliderRow("Current Weight: ${"%.1f".format(currentWeightKg)} kg", currentWeightKg, 35f..200f) { currentWeightKg = it }
                Spacer(modifier = Modifier.height(12.dp))
                ProfileSliderRow("Target Weight: ${"%.1f".format(targetWeightKg)} kg", targetWeightKg, bmiRange.first..bmiRange.second) { targetWeightKg = it }

                Text(
                    text = "Safe BMI range for your height: ${"%.1f".format(bmiRange.first)}kg to ${"%.1f".format(bmiRange.second)}kg",
                    color = MutedText,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(top = 6.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedCard(
            colors = CardDefaults.outlinedCardColors(containerColor = CardDark),
            border = BorderStroke(1.dp, Color(0xFF2D3342))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Schedule, contentDescription = null, tint = Color.White)
                    Text("Workout Schedule", color = Color.White, style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(start = 8.dp))
                }
                Spacer(modifier = Modifier.height(12.dp))
                FlowRow(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    days.forEach { day ->
                        val selected = selectedDays.contains(day)
                        FilterChip(
                            selected = selected,
                            onClick = { if (selected) selectedDays.remove(day) else selectedDays.add(day) },
                            label = { Text(day) },
                            leadingIcon = if (selected) { { Icon(Icons.Default.Check, contentDescription = null) } } else null
                        )
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedButton(onClick = { showTimePicker = true }) {
                    Text(text = "Reminder: ${"%02d:%02d".format(reminderHour, reminderMinute)}")
                }
            }
        }

        Spacer(modifier = Modifier.height(22.dp))

        Button(
            onClick = {
                onComplete(
                    UserProfile(
                        gender = gender,
                        heightCm = heightCm,
                        currentWeightKg = currentWeightKg,
                        targetWeightKg = targetWeightKg,
                        workoutDays = selectedDays.toSet(),
                        reminderHour = reminderHour,
                        reminderMinute = reminderMinute,
                        geminiApiKey = initialProfile.geminiApiKey
                    )
                )
            },
            enabled = selectedDays.isNotEmpty(),
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(14.dp)
        ) { Text("Generate My Plan", fontWeight = FontWeight.Bold) }
    }
}

@Composable
private fun ProfileSliderRow(
    label: String,
    value: Float,
    valueRange: ClosedFloatingPointRange<Float>,
    onValueChange: (Float) -> Unit,
) {
    Text(text = label, color = Color.White, style = MaterialTheme.typography.bodyLarge)
    Slider(value = value, onValueChange = onValueChange, valueRange = valueRange)
}
