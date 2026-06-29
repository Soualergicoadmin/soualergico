package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.R
import com.example.data.Allergy
import com.example.data.Medication
import com.example.ui.theme.Primary
import com.example.ui.theme.PrimaryContainer
import com.example.ui.theme.Secondary
import com.example.viewmodel.MainViewModel

@Composable
fun HomeScreen(
    viewModel: MainViewModel,
    onNavigateToAddAllergy: () -> Unit,
    onNavigateToAddMedication: () -> Unit,
    onNavigateToAllergyList: () -> Unit,
    onNavigateToMedicationList: () -> Unit
) {
    val allergies by viewModel.allergies.collectAsState()
    val medications by viewModel.medications.collectAsState()
    val profile by viewModel.profile.collectAsState()

    val selectedAllergy by viewModel.selectedAllergy.collectAsState()
    val isAllergyModalVisible by viewModel.isAllergyModalVisible.collectAsState()

    val selectedMedication by viewModel.selectedMedication.collectAsState()
    val isMedicationModalVisible by viewModel.isMedicationModalVisible.collectAsState()

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
            verticalArrangement = Arrangement.spacedBy(20.dp)
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
                    Column {
                        Text(
                            text = "AllergySafe",
                            style = MaterialTheme.typography.titleLarge.copy(
                                color = Primary,
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            // Sub-logo icon
                            Icon(
                                imageVector = Icons.Default.HealthAndSafety,
                                contentDescription = "Sub logo",
                                tint = Primary,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = "Sou Alérgico",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    color = Secondary
                                )
                            )
                        }
                    }

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
                        // Generated Headshot image of friendly João Silva
                        Image(
                            painter = painterResource(id = R.drawable.img_profile_avatar),
                            contentDescription = "User Profile Avatar",
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .border(1.dp, Color(0xFFBDC9C8), CircleShape),
                            contentScale = ContentScale.Crop
                        )
                    }
                }
            }

            // Greeting Banner
            item {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = "Olá, ${profile.name.split(" ").firstOrNull() ?: "João"}!",
                        style = MaterialTheme.typography.headlineLarge.copy(
                            color = Color(0xFF181C1C),
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.TaskAlt,
                            contentDescription = "Task complete",
                            tint = Primary,
                            modifier = Modifier.size(20.dp)
                        )
                        Text(
                            text = "Tudo sob controle hoje.",
                            style = MaterialTheme.typography.bodyLarge.copy(
                                color = Primary,
                                fontWeight = FontWeight.Medium
                            )
                        )
                    }
                }
            }

            // Quick Actions: Cadastrar Alergia
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(130.dp)
                        .testTag("home_add_allergy_card"),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(12.dp),
                    border = borderStrokeHelper()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.AddCircle,
                            contentDescription = "Add Icon",
                            tint = Primary,
                            modifier = Modifier.size(28.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Button(
                            onClick = onNavigateToAddAllergy,
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = PrimaryContainer),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(44.dp)
                                .testTag("btn_register_allergy")
                        ) {
                            Text(
                                "Cadastrar Alergia",
                                style = MaterialTheme.typography.labelLarge.copy(color = Color.White)
                            )
                        }
                    }
                }
            }

            // Minhas Alergias List Preview
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.HealthAndSafety,
                            contentDescription = "Alergias header icon",
                            tint = Primary,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "MINHAS ALERGIAS",
                            style = MaterialTheme.typography.labelLarge.copy(
                                color = Secondary,
                                letterSpacing = 1.sp
                            )
                        )
                    }
                    Text(
                        text = "Ver todos",
                        style = MaterialTheme.typography.labelMedium.copy(
                            color = Primary,
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier
                            .clickable(onClick = onNavigateToAllergyList)
                            .testTag("home_ver_todos_allergies")
                    )
                }
            }

            // Display top 2 allergies as preview
            val allergyPreview = allergies.take(2)
            if (allergyPreview.isEmpty()) {
                item {
                    EmptyStatePlaceholder(text = "Nenhuma alergia cadastrada.")
                }
            } else {
                items(allergyPreview) { allergy ->
                    AllergyPreviewItem(allergy = allergy, onClick = {
                        viewModel.selectAllergy(allergy)
                    })
                }
            }

            // Medicamentos Section
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.MedicalServices,
                            contentDescription = "Medication list icon",
                            tint = Primary,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "MEDICAMENTOS EM USO",
                            style = MaterialTheme.typography.labelLarge.copy(
                                color = Secondary,
                                letterSpacing = 1.sp
                            )
                        )
                    }
                    Text(
                        text = "Ver todos",
                        style = MaterialTheme.typography.labelMedium.copy(
                            color = Primary,
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier
                            .clickable(onClick = onNavigateToMedicationList)
                            .testTag("home_ver_todos_medications")
                    )
                }
            }

            // Medication low elevation block
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF0F4F3)),
                    shape = RoundedCornerShape(12.dp),
                    border = borderStrokeHelper()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Cadastrar Medicamento Button
                        Button(
                            onClick = onNavigateToAddMedication,
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Primary),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(44.dp)
                                .testTag("btn_register_medication")
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AddCircle,
                                    contentDescription = "Add med icon",
                                    tint = Color.White,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Cadastrar Medicamento", color = Color.White, fontWeight = FontWeight.Bold)
                            }
                        }

                        // Next medication teaser (e.g. Anti-histamínico)
                        val nextMed = medications.firstOrNull { !it.isContinuous }
                            ?: medications.firstOrNull()

                        if (nextMed != null) {
                            Column {
                                Divider(color = Color(0xFFBDC9C8).copy(alpha = 0.5f))
                                Spacer(modifier = Modifier.height(12.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        "Próxima Medicação",
                                        style = MaterialTheme.typography.labelLarge.copy(
                                            color = Primary,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                    )
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.NotificationsActive,
                                            contentDescription = "Alert active",
                                            tint = Primary,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Text(
                                            nextMed.nextDoseTime,
                                            style = MaterialTheme.typography.labelMedium.copy(
                                                color = Secondary,
                                                fontWeight = FontWeight.Bold
                                            )
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            viewModel.selectMedication(nextMed)
                                        },
                                    colors = CardDefaults.cardColors(containerColor = Color.White),
                                    shape = RoundedCornerShape(10.dp),
                                    border = borderStrokeHelper(color = Primary)
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(12.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                                        ) {
                                            Box(
                                                modifier = Modifier
                                                    .size(40.dp)
                                                    .clip(RoundedCornerShape(8.dp))
                                                    .background(Primary.copy(alpha = 0.1f)),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.MedicalServices,
                                                    contentDescription = "Pill icon",
                                                    tint = Primary,
                                                    modifier = Modifier.size(22.dp)
                                                )
                                            }
                                            Column {
                                                Text(
                                                    nextMed.name,
                                                    fontWeight = FontWeight.Bold,
                                                    color = Color(0xFF181C1C)
                                                )
                                                Text(
                                                    "${nextMed.dosage} • ${nextMed.startedDate}",
                                                    style = MaterialTheme.typography.labelMedium.copy(
                                                        color = Secondary
                                                    )
                                                )
                                            }
                                        }
                                        Icon(
                                            imageVector = Icons.Default.Info,
                                            contentDescription = "info icon",
                                            tint = Secondary
                                        )
                                    }
                                }
                            }
                        }

                        // Premium subscription bar
                        Column {
                            Divider(color = Color(0xFFBDC9C8).copy(alpha = 0.5f))
                            Spacer(modifier = Modifier.height(12.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Stars,
                                    contentDescription = "Subscription indicator",
                                    tint = Primary,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Assinatura Ativa | Premium",
                                    fontSize = 12.sp,
                                    color = Color(0xFF181C1C),
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }
                }
            }
        }

        // Selected Allergy Details Modal (Peanut/Amendoim details)
        if (isAllergyModalVisible && selectedAllergy != null) {
            AllergyDetailsModal(
                allergy = selectedAllergy!!,
                onDismiss = { viewModel.closeAllergyModal() }
            )
        }

        // Selected Medication "Tomei" Modal
        if (isMedicationModalVisible && selectedMedication != null) {
            MedicationDoseModal(
                medication = selectedMedication!!,
                onDismiss = { viewModel.closeMedicationModal() },
                onTaken = {
                    viewModel.closeMedicationModal()
                }
            )
        }
    }
}

@Composable
fun AllergyPreviewItem(allergy: Allergy, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(10.dp),
        border = borderStrokeHelper()
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // "Novo" badge for gluten/first item
            if (allergy.name == "Glúten") {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(top = 4.dp, right = 4.dp)
                        .background(Primary, RoundedCornerShape(8.dp))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        "Novo",
                        color = Color.White,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    val icon = when (allergy.severity) {
                        "Grave" -> Icons.Default.Warning
                        "Intolerância" -> Icons.Default.Eco
                        "Medicamentosa" -> Icons.Default.MedicalServices
                        else -> Icons.Default.Air
                    }
                    val iconColor = if (allergy.severity == "Grave" || allergy.severity == "Medicamentosa") {
                        MaterialTheme.colorScheme.error
                    } else {
                        Primary
                    }
                    val iconBg = iconColor.copy(alpha = 0.1f)

                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(iconBg),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = "Allergy icon",
                            tint = iconColor,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Column {
                        Text(
                            text = allergy.name,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF181C1C)
                        )
                        Text(
                            text = allergy.severity,
                            style = MaterialTheme.typography.labelMedium.copy(
                                color = Secondary
                            )
                        )
                    }
                }

                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = "Ver detalhes",
                    tint = Secondary
                )
            }
        }
    }
}

@Composable
fun AllergyDetailsModal(allergy: Allergy, onDismiss: () -> Unit) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(vertical = 24.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Modal Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.MedicalInformation,
                            contentDescription = "Info icon",
                            tint = Primary
                        )
                        Text(
                            "Detalhes: ${allergy.name}",
                            style = MaterialTheme.typography.titleLarge.copy(
                                color = Primary,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = Secondary
                        )
                    }
                }

                Divider(color = Color(0xFFBDC9C8).copy(alpha = 0.5f))

                // Scrollable details inside the modal to support varied content heights safely
                Column(
                    modifier = Modifier.weight(1f, fill = false),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Causa Section
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Causes Icon",
                                tint = Primary,
                                modifier = Modifier.size(18.dp)
                            )
                            Text(
                                "O QUE CAUSA:",
                                style = MaterialTheme.typography.labelLarge.copy(
                                    color = Color(0xFF181C1C),
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }

                        Card(
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFF0F4F3)),
                            shape = RoundedCornerShape(8.dp),
                            border = BorderStroke(1.dp, Primary.copy(alpha = 0.2f))
                        ) {
                            Box(modifier = Modifier.padding(12.dp)) {
                                Text(
                                    text = allergy.symptoms,
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        color = Secondary,
                                        lineHeight = 20.sp
                                    )
                                )
                            }
                        }
                    }

                    // Emergency Medication Section
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Emergency,
                                contentDescription = "Emergency Icon",
                                tint = MaterialTheme.colorScheme.error,
                                modifier = Modifier.size(18.dp)
                            )
                            Text(
                                "MEDICAÇÃO DE EMERGÊNCIA:",
                                style = MaterialTheme.typography.labelLarge.copy(
                                    color = Color(0xFF181C1C),
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }

                        Card(
                            colors = CardDefaults.cardColors(containerColor = PrimaryContainer),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.Top
                                ) {
                                    Column {
                                        Text(
                                            allergy.emergencyMedicationName,
                                            style = MaterialTheme.typography.titleLarge.copy(
                                                color = Color.White,
                                                fontWeight = FontWeight.Bold
                                            )
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Box(
                                            modifier = Modifier
                                                .background(
                                                    Color.White.copy(alpha = 0.2f),
                                                    CircleShape
                                                )
                                                .padding(horizontal = 10.dp, vertical = 2.dp)
                                        ) {
                                            Text(
                                                "Dose: ${allergy.emergencyMedicationDose}",
                                                color = Color.White,
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Medium
                                            )
                                        }
                                    }

                                    if (allergy.emergencyMedicationUrgent) {
                                        Card(
                                            colors = CardDefaults.cardColors(containerColor = Color.White),
                                            shape = RoundedCornerShape(8.dp)
                                        ) {
                                            Box(modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)) {
                                                Text(
                                                    "Urgente",
                                                    color = Primary,
                                                    fontSize = 11.sp,
                                                    fontWeight = FontWeight.Bold
                                                )
                                            }
                                        }
                                    }
                                }

                                Divider(color = Color.White.copy(alpha = 0.2f))

                                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                                    Text(
                                        "Instruções:",
                                        style = MaterialTheme.typography.labelMedium.copy(
                                            color = Color.White,
                                            fontWeight = FontWeight.Bold
                                        )
                                    )
                                    Text(
                                        allergy.instructions,
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                            color = Color.White.copy(alpha = 0.9f)
                                        )
                                    )
                                }
                            }
                        }
                    }

                    // Epinephrine kit generated illustration
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(130.dp)
                    ) {
                        Box(modifier = Modifier.fillMaxSize()) {
                            Image(
                                painter = painterResource(id = R.drawable.img_emergency_kit),
                                contentDescription = "Medical Kit illustration",
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
                                                Color.Black.copy(alpha = 0.6f)
                                            )
                                        )
                                    )
                            )
                            Text(
                                "Sempre mantenha sua medicação ao alcance.",
                                color = Color.White,
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.SemiBold
                                ),
                                modifier = Modifier
                                    .align(Alignment.BottomStart)
                                    .padding(12.dp)
                            )
                        }
                    }
                }

                // Action Footer button
                OutlinedButton(
                    onClick = onDismiss,
                    border = BorderStroke(1.5.dp, Primary),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Primary),
                    shape = CircleShape,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                ) {
                    Text(
                        "Fechar Detalhes",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                }
            }
        }
    }
}

@Composable
fun MedicationDoseModal(
    medication: Medication,
    onDismiss: () -> Unit,
    onTaken: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(horizontal = 16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Column {
                        Text(
                            medication.name,
                            style = MaterialTheme.typography.headlineMedium.copy(
                                color = Primary,
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Text(
                            "Detalhes da Dose",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = Secondary
                            )
                        )
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = Secondary
                        )
                    }
                }

                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.MedicalServices,
                            contentDescription = "Dose icon",
                            tint = Primary,
                            modifier = Modifier.size(20.dp)
                        )
                        Text(
                            "Dosagem: ${medication.dosage}",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Schedule,
                            contentDescription = "Time icon",
                            tint = Primary,
                            modifier = Modifier.size(20.dp)
                        )
                        Text(
                            "Horário: ${medication.nextDoseTime}",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }

                Button(
                    onClick = onTaken,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Primary),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                ) {
                    Text(
                        "Tomei",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                }
            }
        }
    }
}

@Composable
fun EmptyStatePlaceholder(text: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(10.dp),
        border = borderStrokeHelper()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                color = Secondary,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

// Global border helper for nice M3 container outline clarity style
@Composable
fun borderStrokeHelper(color: Color = Color(0xFFBDC9C8).copy(alpha = 0.4f)) =
    BorderStroke(1.dp, color)
