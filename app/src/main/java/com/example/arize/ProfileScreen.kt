package com.example.arize

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Feedback
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfilePage(
    profile: UserProfile,
    onSave: (UserProfile) -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    var reminderHour by rememberSaveable { mutableIntStateOf(profile.reminderHour) }
    var reminderMinute by rememberSaveable { mutableIntStateOf(profile.reminderMinute) }
    val selectedDays = remember { mutableStateListOf(*profile.workoutDays.toTypedArray()) }
    var showTimePicker by remember { mutableStateOf(false) }
    val picker = rememberTimePickerState(initialHour = reminderHour, initialMinute = reminderMinute)

    if (showTimePicker) {
        TimePickerDialog(
            onDismissRequest = { showTimePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    reminderHour = picker.hour
                    reminderMinute = picker.minute
                    showTimePicker = false
                }) { Text("Confirm") }
            },
            dismissButton = { TextButton(onClick = { showTimePicker = false }) { Text("Cancel") } }
        ) { TimePicker(state = picker) }
    }

    val days = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")

    LazyColumn(
        modifier = modifier.fillMaxSize().background(AppDark),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text("Profile & Settings", color = Color.White, style = androidx.compose.material3.MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
            Text("Manage your training schedule and reminders.", color = MutedText, modifier = Modifier.padding(top = 6.dp))
        }

        item {
            Card(colors = CardDefaults.cardColors(containerColor = CardDark), shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp)) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text("Workout Days", color = Color.White, fontWeight = FontWeight.SemiBold)
                    Spacer(modifier = Modifier.height(8.dp))
                    FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        days.forEach { day ->
                            val selected = selectedDays.contains(day)
                            FilterChip(
                                selected = selected,
                                onClick = {
                                    if (selected) selectedDays.remove(day) else selectedDays.add(day)
                                },
                                label = { Text(day) },
                                border = androidx.compose.foundation.BorderStroke(1.dp, if (selected) AccentBlue else Color(0xFF2A3142)),
                                colors = androidx.compose.material3.FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = AccentBlue,
                                    containerColor = Color(0xFF171C28),
                                    selectedLabelColor = Color.White,
                                    labelColor = Color(0xFFD5DBEA)
                                )
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedButton(
                        onClick = { showTimePicker = true },
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF2A3142))
                    ) {
                        Icon(Icons.Default.Schedule, contentDescription = null)
                        Text("Reminder ${"%02d:%02d".format(reminderHour, reminderMinute)}", modifier = Modifier.padding(start = 6.dp), color = Color.White)
                    }
                }
            }
        }

        item {
            Card(colors = CardDefaults.cardColors(containerColor = CardDark), shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp)) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("Support", color = Color.White, fontWeight = FontWeight.SemiBold)
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
                        OutlinedButton(
                            onClick = {
                                val intent = Intent(Intent.ACTION_SENDTO).apply {
                                    data = Uri.parse("mailto:arize.app.feedback@gmail.com")
                                    putExtra(Intent.EXTRA_SUBJECT, "Arize App Feedback")
                                }
                                context.startActivity(intent)
                            },
                            modifier = Modifier.weight(1f),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF2A3142))
                        ) {
                            Icon(Icons.Default.Feedback, contentDescription = null, tint = Color.White)
                            Text("Feedback", color = Color.White, modifier = Modifier.padding(start = 6.dp))
                        }
                        OutlinedButton(
                            onClick = {
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.example.arize"))
                                runCatching { context.startActivity(intent) }
                                    .onFailure {
                                        context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.example.arize")))
                                    }
                            },
                            modifier = Modifier.weight(1f),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF2A3142))
                        ) {
                            Icon(Icons.Default.Star, contentDescription = null, tint = Color(0xFFFFD166))
                            Text("Rate App", color = Color.White, modifier = Modifier.padding(start = 6.dp))
                        }
                    }
                }
            }
        }

        item {
            Button(
                onClick = {
                    onSave(
                        profile.copy(
                            workoutDays = selectedDays.toSet(),
                            reminderHour = reminderHour,
                            reminderMinute = reminderMinute
                        )
                    )
                },
                modifier = Modifier.fillMaxWidth().height(54.dp)
            ) { Text("Save Changes", fontWeight = FontWeight.Bold) }
        }
    }
}
