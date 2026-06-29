package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.Primary
import com.example.ui.theme.PrimaryContainer
import com.example.ui.theme.Secondary
import com.example.viewmodel.MainViewModel

@Composable
fun AddMedicationScreen(
    viewModel: MainViewModel,
    onBack: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var dosage by remember { mutableStateOf("") }
    var isContinuous by remember { mutableStateOf(false) }

    // Alarm configurations
    var hour by remember { mutableStateOf("08") }
    var minute by remember { mutableStateOf("00") }
    var selectedFrequency by remember { mutableStateOf("Uma vez ao dia") }
    var notificationsEnabled by remember { mutableStateOf(true) }

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF6FAF9))
    ) {
        // Top App Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onBack,
                modifier = Modifier.testTag("btn_back_add_medication")
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Voltar",
                    tint = Primary
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Adicionar Medicação",
                style = MaterialTheme.typography.headlineMedium.copy(
                    color = Primary,
                    fontWeight = FontWeight.Bold
                )
            )
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(scrollState)
                .padding(horizontal = 20.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Header Introduction
            Text(
                text = "Cadastre seus medicamentos, dosagens e configure os alarmes para os horários corretos.",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = Secondary,
                    lineHeight = 22.sp
                )
            )

            // Form Card Section (gray background, round-lg)
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5)),
                shape = RoundedCornerShape(12.dp),
                border = borderStrokeHelper()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Medicine Name Input
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(
                            text = "Nome do Medicamento",
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF181C1C)
                        )
                        OutlinedTextField(
                            value = name,
                            onValueChange = { name = it },
                            placeholder = { Text("Ex: Anti-histamínico, Corticoide...") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("input_med_name"),
                            shape = RoundedCornerShape(8.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = Color.White,
                                unfocusedContainerColor = Color.White,
                                focusedIndicatorColor = PrimaryContainer,
                                unfocusedIndicatorColor = Color(0xFFE0E0E0)
                            )
                        )
                    }

                    // Dosage Input
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(
                            text = "Dosagem",
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF181C1C)
                        )
                        OutlinedTextField(
                            value = dosage,
                            onValueChange = { dosage = it },
                            placeholder = { Text("Ex: 10mg, 5ml, 1 comprimido...") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("input_med_dosage"),
                            shape = RoundedCornerShape(8.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = Color.White,
                                unfocusedContainerColor = Color.White,
                                focusedIndicatorColor = PrimaryContainer,
                                unfocusedIndicatorColor = Color(0xFFE0E0E0)
                            )
                        )
                    }

                    Divider(color = Color(0xFFBDC9C8).copy(alpha = 0.3f))

                    // Medicamento de Uso Contínuo Switch
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                "Medicamento de Uso Contínuo",
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF181C1C)
                            )
                            Text(
                                "Selecione se você utiliza este medicamento diariamente.",
                                fontSize = 11.sp,
                                color = Secondary
                            )
                        }
                        Switch(
                            checked = isContinuous,
                            onCheckedChange = { isContinuous = it },
                            colors = SwitchDefaults.colors(checkedThumbColor = Primary)
                        )
                    }
                }
            }

            // Alarm Configuration Section
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5)),
                shape = RoundedCornerShape(12.dp),
                border = borderStrokeHelper()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Schedule,
                            contentDescription = "Alarm config",
                            tint = Primary,
                            modifier = Modifier.size(22.dp)
                        )
                        Text(
                            "Configuração de Alarmes",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF181C1C)
                            )
                        )
                    }

                    // Time Picker visualizer with text boxes for hours & minutes
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            OutlinedTextField(
                                value = hour,
                                onValueChange = { if (it.length <= 2) hour = it },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                modifier = Modifier
                                    .width(72.dp)
                                    .testTag("input_hour"),
                                shape = RoundedCornerShape(12.dp),
                                textStyle = androidx.compose.ui.text.TextStyle(
                                    fontSize = 28.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Primary,
                                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                ),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedContainerColor = Color.White,
                                    unfocusedContainerColor = Color.White,
                                    focusedIndicatorColor = Primary,
                                    unfocusedIndicatorColor = Color(0xFFE0E0E0)
                                )
                            )

                            Text(":", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Secondary)

                            OutlinedTextField(
                                value = minute,
                                onValueChange = { if (it.length <= 2) minute = it },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                modifier = Modifier
                                    .width(72.dp)
                                    .testTag("input_minute"),
                                shape = RoundedCornerShape(12.dp),
                                textStyle = androidx.compose.ui.text.TextStyle(
                                    fontSize = 28.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Primary,
                                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                ),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedContainerColor = Color.White,
                                    unfocusedContainerColor = Color.White,
                                    focusedIndicatorColor = Primary,
                                    unfocusedIndicatorColor = Color(0xFFE0E0E0)
                                )
                            )
                        }
                    }

                    // Frequency buttons grid selector (4 options)
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(
                            "Frequência",
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF181C1C)
                        )

                        val frequencies = listOf(
                            "A cada 8 horas",
                            "A cada 12 horas",
                            "Uma vez ao dia",
                            "Personalizado"
                        )

                        // 2x2 grid layout
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                FrequencyButton(
                                    text = frequencies[0],
                                    isSelected = selectedFrequency == frequencies[0],
                                    onClick = { selectedFrequency = frequencies[0] },
                                    modifier = Modifier.weight(1f)
                                )
                                FrequencyButton(
                                    text = frequencies[1],
                                    isSelected = selectedFrequency == frequencies[1],
                                    onClick = { selectedFrequency = frequencies[1] },
                                    modifier = Modifier.weight(1f)
                                )
                            }
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                FrequencyButton(
                                    text = frequencies[2],
                                    isSelected = selectedFrequency == frequencies[2],
                                    onClick = { selectedFrequency = frequencies[2] },
                                    modifier = Modifier.weight(1f)
                                )
                                FrequencyButton(
                                    text = frequencies[3],
                                    isSelected = selectedFrequency == frequencies[3],
                                    onClick = { selectedFrequency = frequencies[3] },
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }

                    Divider(color = Color(0xFFBDC9C8).copy(alpha = 0.3f))

                    // Toggle notifications
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                "Ativar Alertas e Notificações",
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF181C1C)
                            )
                            Text(
                                "Receba lembretes no seu celular",
                                fontSize = 11.sp,
                                color = Secondary
                            )
                        }
                        Switch(
                            checked = notificationsEnabled,
                            onCheckedChange = { notificationsEnabled = it },
                            colors = SwitchDefaults.colors(checkedThumbColor = Primary)
                        )
                    }
                }
            }

            // Health reminder banner visual section (Bento elements)
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    Image(
                        painter = painterResource(id = R.drawable.img_health_reminder),
                        contentDescription = "Medical reminder illustration",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        Color.Black.copy(alpha = 0.5f)
                                    )
                                )
                            )
                    )
                    Text(
                        "Mantenha seu tratamento sempre em dia para maior segurança.",
                        color = Color.White,
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = PrimaryContainer
                        ),
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(16.dp)
                    )
                }
            }
        }

        // Footer action button
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(16.dp)
        ) {
            Button(
                onClick = {
                    if (name.isNotBlank()) {
                        val formattedHour = hour.padStart(2, '0')
                        val formattedMinute = minute.padStart(2, '0')
                        viewModel.saveMedication(
                            name = name,
                            dosage = dosage.ifBlank { "Dose recomendada" },
                            isContinuous = isContinuous,
                            frequency = selectedFrequency,
                            nextDoseTime = "$formattedHour:$formattedMinute",
                            startedDate = "12 Out", // Sample default
                            durationDays = if (isContinuous) 0 else 7,
                            notificationEnabled = notificationsEnabled
                        )
                        onBack()
                    }
                },
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryContainer),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("btn_save_medication"),
                enabled = name.isNotBlank()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Save,
                        contentDescription = "Save icon",
                        tint = Color.White
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        "Salvar Medicação e Alarme",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
            }
        }
    }
}

@Composable
fun FrequencyButton(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .height(44.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) PrimaryContainer else Color.White
        ),
        border = BorderStroke(1.dp, if (isSelected) PrimaryContainer else Color(0xFFE0E0E0))
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = if (isSelected) Color.White else Secondary
            )
        }
    }
}
