package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.viewmodel.MainViewModel

@Composable
fun AddAllergyScreen(
    viewModel: MainViewModel,
    onBack: () -> Unit
) {
    var activeTab by remember { mutableStateOf("alimento") } // "alimento" or "medicamento"

    var name by remember { mutableStateOf("") }
    var symptoms by remember { mutableStateOf("") }
    var instructions by remember { mutableStateOf("") }

    // Emergency medication fields (Advanced fully working addition)
    var medName by remember { mutableStateOf("") }
    var medDose by remember { mutableStateOf("") }
    var isUrgent by remember { mutableStateOf(false) }

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
                modifier = Modifier.testTag("btn_back_add_allergy")
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Voltar",
                    tint = Primary
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Nova Alergia",
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
            // Segmented Toggle / Tabs
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFEBEFEE))
                    .padding(4.dp)
            ) {
                // Alimento Tab
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (activeTab == "alimento") PrimaryContainer else Color.Transparent)
                        .clickable { activeTab = "alimento" }
                        .padding(vertical = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "Alimento",
                        color = if (activeTab == "alimento") Color.White else Secondary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }

                // Medicamento Tab
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (activeTab == "medicamento") PrimaryContainer else Color.Transparent)
                        .clickable { activeTab = "medicamento" }
                        .padding(vertical = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "Medicamento",
                        color = if (activeTab == "medicamento") Color.White else Secondary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
            }

            // Input: Allergy Item Name
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(
                    text = if (activeTab == "alimento") "Sou alérgico ao alimento" else "Sou alérgico ao medicamento",
                    style = MaterialTheme.typography.labelLarge.copy(
                        color = Color(0xFF3E4949),
                        fontWeight = FontWeight.SemiBold
                    )
                )
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    placeholder = {
                        Text(if (activeTab == "alimento") "e.g., Trigo, Leite..." else "e.g., Dipirona, Aspirina...")
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("input_allergy_name"),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color(0xFFF0F4F3),
                        unfocusedContainerColor = Color(0xFFF0F4F3),
                        focusedIndicatorColor = PrimaryContainer,
                        unfocusedIndicatorColor = Color.Transparent
                    )
                )
            }

            // Input: Symptoms
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(
                    text = if (activeTab == "alimento") "Este alimento pode me causar" else "Este medicamento pode me causar",
                    style = MaterialTheme.typography.labelLarge.copy(
                        color = Color(0xFF3E4949),
                        fontWeight = FontWeight.SemiBold
                    )
                )
                OutlinedTextField(
                    value = symptoms,
                    onValueChange = { symptoms = it },
                    placeholder = { Text("Descreva os sintomas...") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(110.dp)
                        .testTag("input_allergy_symptoms"),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color(0xFFF0F4F3),
                        unfocusedContainerColor = Color(0xFFF0F4F3),
                        focusedIndicatorColor = PrimaryContainer,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    maxLines = 4
                )
            }

            // Input: Medical advice instructions
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(
                    text = "O que meu médico indica fazer",
                    style = MaterialTheme.typography.labelLarge.copy(
                        color = Color(0xFF3E4949),
                        fontWeight = FontWeight.SemiBold
                    )
                )
                OutlinedTextField(
                    value = instructions,
                    onValueChange = { instructions = it },
                    placeholder = { Text("Orientações médicas...") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(110.dp)
                        .testTag("input_allergy_instructions"),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color(0xFFF0F4F3),
                        unfocusedContainerColor = Color(0xFFF0F4F3),
                        focusedIndicatorColor = PrimaryContainer,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    maxLines = 4
                )
            }

            // Advanced Emergency Medication Section block (To populate full Entity parameters)
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF0F4F3).copy(alpha = 0.5f)),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        "Medicação de Emergência",
                        fontWeight = FontWeight.Bold,
                        color = Primary
                    )

                    OutlinedTextField(
                        value = medName,
                        onValueChange = { medName = it },
                        placeholder = { Text("e.g., Anti-histamínico, Adrenalina") },
                        label = { Text("Nome do Medicamento") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        OutlinedTextField(
                            value = medDose,
                            onValueChange = { medDose = it },
                            placeholder = { Text("e.g., 10mg, 1 comprimido") },
                            label = { Text("Dosagem") },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(8.dp)
                        )

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier
                                .weight(1f)
                                .align(Alignment.CenterVertically)
                        ) {
                            Text("Urgente", style = MaterialTheme.typography.bodyMedium)
                            Switch(
                                checked = isUrgent,
                                onCheckedChange = { isUrgent = it },
                                colors = SwitchDefaults.colors(checkedThumbColor = Primary)
                            )
                        }
                    }
                }
            }

            // Illustration Placeholder Icon (Bento-style clinical visual feel)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(96.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFEBEFEE)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (activeTab == "alimento") Icons.Default.Restaurant else Icons.Default.Medication,
                        contentDescription = "Category Icon",
                        tint = Secondary,
                        modifier = Modifier.size(36.dp)
                    )
                }
            }
        }

        // Fixed Action Bottom area
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFF6FAF9))
                .padding(20.dp)
        ) {
            Button(
                onClick = {
                    if (name.isNotBlank()) {
                        viewModel.saveAllergy(
                            name = name,
                            type = activeTab,
                            severity = if (activeTab == "alimento") "Alimentar" else "Medicamentosa",
                            symptoms = symptoms.ifBlank { "Sem sintomas cadastrados." },
                            instructions = instructions.ifBlank { "Nenhuma recomendação médica cadastrada." },
                            medicationName = medName.ifBlank { "Sintomáticos" },
                            medicationDose = medDose.ifBlank { "Conforme necessário" },
                            urgent = isUrgent
                        )
                        onBack()
                    }
                },
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryContainer),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("btn_save_allergy"),
                enabled = name.isNotBlank()
            ) {
                Text(
                    text = "Salvar Alergia",
                    style = MaterialTheme.typography.titleLarge.copy(
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        }
    }
}
