package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.Medication
import com.example.ui.theme.Primary
import com.example.ui.theme.PrimaryContainer
import com.example.ui.theme.Secondary
import com.example.viewmodel.MainViewModel

@Composable
fun MedicationListScreen(
    viewModel: MainViewModel,
    onNavigateToAddMedication: () -> Unit
) {
    val medications by viewModel.medications.collectAsState()
    val selectedToDelete by viewModel.selectedMedicationsToDelete.collectAsState()

    val isMedicationModalVisible by viewModel.isMedicationModalVisible.collectAsState()
    val selectedMedication by viewModel.selectedMedication.collectAsState()

    val continuousMeds = medications.filter { it.isContinuous }
    val temporaryMeds = medications.filter { !it.isContinuous }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF6FAF9))
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 80.dp), // Height for Bottom Navigation
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header Top App Bar Section
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        IconButton(onClick = { /* Menu Action */ }) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = "Menu Icon",
                                tint = Primary
                            )
                        }
                        Text(
                            text = "Meus Medicamentos",
                            style = MaterialTheme.typography.titleLarge.copy(
                                color = Primary,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }

                    // Profile avatar round
                    Image(
                        painter = painterResource(id = R.drawable.img_profile_avatar),
                        contentDescription = "User Profile Avatar",
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                }
            }

            // Quick Operations Row (Atualizar, Excluir)
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Excluir selected button
                        if (selectedToDelete.isNotEmpty()) {
                            Button(
                                onClick = { viewModel.deleteSelectedMedications() },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.error.copy(alpha = 0.1f),
                                    contentColor = MaterialTheme.colorScheme.error
                                ),
                                border = borderStrokeHelper(color = MaterialTheme.colorScheme.error.copy(alpha = 0.3f)),
                                shape = RoundedCornerShape(8.dp),
                                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                                modifier = Modifier.testTag("med_btn_excluir")
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "Excluir Icon",
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Text("Excluir (${selectedToDelete.size})", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                                }
                            }
                        }
                    }
                }
            }

            // Section 1: Uso Contínuo
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "USO CONTÍNUO",
                        style = MaterialTheme.typography.labelLarge.copy(
                            color = Secondary,
                            letterSpacing = 1.sp
                        )
                    )
                    Box(
                        modifier = Modifier
                            .background(Primary.copy(alpha = 0.1f), CircleShape)
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "${continuousMeds.size} Ativos",
                            color = Primary,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            if (continuousMeds.isEmpty()) {
                item {
                    EmptyStatePlaceholder(text = "Nenhum medicamento de uso contínuo.")
                }
            } else {
                items(continuousMeds) { med ->
                    ContinuousMedCard(
                        medication = med,
                        isSelected = selectedToDelete.contains(med.id),
                        onCheckedChange = { viewModel.toggleMedicationSelection(med.id) },
                        onClick = { viewModel.selectMedication(med) }
                    )
                }
            }

            // Section 2: Uso Temporário
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "USO TEMPORÁRIO",
                        style = MaterialTheme.typography.labelLarge.copy(
                            color = Secondary,
                            letterSpacing = 1.sp
                        )
                    )
                    Box(
                        modifier = Modifier
                            .background(
                                MaterialTheme.colorScheme.error.copy(alpha = 0.1f),
                                CircleShape
                            )
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "Tratamento em curso",
                            color = MaterialTheme.colorScheme.error,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            if (temporaryMeds.isEmpty()) {
                item {
                    EmptyStatePlaceholder(text = "Nenhum tratamento temporário ativo.")
                }
            } else {
                items(temporaryMeds) { med ->
                    TemporaryMedCard(
                        medication = med,
                        isSelected = selectedToDelete.contains(med.id),
                        onCheckedChange = { viewModel.toggleMedicationSelection(med.id) },
                        onClick = { viewModel.selectMedication(med) }
                    )
                }
            }

            // Aesthetic Visual Break Health Reminder Illustration banner (Bento box style)
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(130.dp),
                    shape = RoundedCornerShape(16.dp),
                    border = borderStrokeHelper()
                ) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        Image(
                            painter = painterResource(id = R.drawable.img_health_reminder),
                            contentDescription = "Health reminder cover",
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
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.Bottom
                        ) {
                            Text(
                                "Lembrete de Saúde",
                                color = Color.White,
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold
                                )
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                "Mantenha-se hidratado ao tomar seus medicamentos.",
                                color = Color.White.copy(alpha = 0.85f),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Normal
                            )
                        }
                    }
                }
            }
        }

        // FAB to add medication
        FloatingActionButton(
            onClick = onNavigateToAddMedication,
            containerColor = PrimaryContainer,
            contentColor = Color.White,
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 96.dp, end = 16.dp) // Offset above bottom nav bar
                .testTag("fab_add_medication")
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Adicionar Medicamento Icon",
                modifier = Modifier.size(28.dp)
            )
        }

        // Details dialog if medication clicked
        if (isMedicationModalVisible && selectedMedication != null) {
            MedicationDoseModal(
                medication = selectedMedication!!,
                onDismiss = { viewModel.closeMedicationModal() },
                onTaken = { viewModel.closeMedicationModal() }
            )
        }
    }
}

@Composable
fun ContinuousMedCard(
    medication: Medication,
    isSelected: Boolean,
    onCheckedChange: () -> Unit,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(12.dp),
        border = borderStrokeHelper()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.weight(1f)
            ) {
                Checkbox(
                    checked = isSelected,
                    onCheckedChange = { onCheckedChange() },
                    colors = CheckboxDefaults.colors(checkedColor = Primary)
                )

                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Primary.copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.MedicalServices,
                        contentDescription = "Continuous med medication icon",
                        tint = Primary,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Column {
                    Text(
                        medication.name,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF181C1C)
                    )
                    Text(
                        "${medication.dosage} • ${medication.frequency}",
                        style = MaterialTheme.typography.labelMedium.copy(
                            color = Secondary
                        )
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Schedule,
                            contentDescription = "schedule",
                            tint = Primary,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            "Próximo: ${medication.nextDoseTime}",
                            fontSize = 11.sp,
                            color = Primary,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            IconButton(onClick = onClick) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "Options",
                    tint = Secondary
                )
            }
        }
    }
}

@Composable
fun TemporaryMedCard(
    medication: Medication,
    isSelected: Boolean,
    onCheckedChange: () -> Unit,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(12.dp),
        border = borderStrokeHelper()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Checkbox(
                        checked = isSelected,
                        onCheckedChange = { onCheckedChange() },
                        colors = CheckboxDefaults.colors(checkedColor = Primary)
                    )

                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(PrimaryContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.MedicalServices,
                            contentDescription = "Temporary med medication icon",
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Column {
                        Text(
                            medication.name,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF181C1C)
                        )
                        Text(
                            "${medication.dosage} • ${medication.frequency}",
                            style = MaterialTheme.typography.labelMedium.copy(
                                color = Secondary
                            )
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.History,
                                contentDescription = "History icon",
                                tint = Primary,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                "Iniciado em ${medication.startedDate}",
                                fontSize = 11.sp,
                                color = Primary,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                IconButton(onClick = onClick) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "Options",
                        tint = Secondary
                    )
                }
            }

            // Treatment Progress Indicator (Dia 3 de 7)
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    Text(
                        "Progresso do tratamento",
                        fontSize = 12.sp,
                        color = Secondary,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        "Dia ${medication.currentDay} de ${medication.durationDays}",
                        fontSize = 12.sp,
                        color = Primary,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Smooth styled linear progress bar
                val progress = if (medication.durationDays > 0) {
                    medication.currentDay.toFloat() / medication.durationDays.toFloat()
                } else {
                    1f
                }
                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(CircleShape),
                    color = Primary,
                    trackColor = Color(0xFFEBEFEE)
                )

                Spacer(modifier = Modifier.height(2.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.NotificationsActive,
                        contentDescription = "Next alarm",
                        tint = Secondary,
                        modifier = Modifier.size(14.dp)
                    )
                    Text(
                        text = "PRÓXIMA DOSE EM 2H 15M",
                        fontSize = 11.sp,
                        color = Secondary,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp
                    )
                }
            }
        }
    }
}
