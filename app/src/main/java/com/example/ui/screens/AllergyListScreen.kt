package com.example.ui.screens

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.Allergy
import com.example.ui.theme.Primary
import com.example.ui.theme.PrimaryContainer
import com.example.ui.theme.Secondary
import com.example.viewmodel.MainViewModel

@Composable
fun AllergyListScreen(
    viewModel: MainViewModel,
    onNavigateToAddAllergy: () -> Unit,
    onClose: () -> Unit
) {
    val allergies by viewModel.allergies.collectAsState()
    val selectedToDelete by viewModel.selectedAllergiesToDelete.collectAsState()

    val selectedAllergy by viewModel.selectedAllergy.collectAsState()
    val isAllergyModalVisible by viewModel.isAllergyModalVisible.collectAsState()

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
            // Header Top App Bar Section (Modal-style Overlay with Close X button)
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
                        IconButton(onClick = { /* Menu / list action */ }) {
                            Icon(
                                imageVector = Icons.Default.ListAlt,
                                contentDescription = "List icon",
                                tint = Primary
                            )
                        }
                        Text(
                            text = "Minhas Alergias",
                            style = MaterialTheme.typography.titleLarge.copy(
                                color = Primary,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }

                    IconButton(
                        onClick = onClose,
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(Color(0xFFEBEFEE))
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Fechar",
                            tint = Secondary
                        )
                    }
                }
            }

            // Contextual Advice Banner
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF0F4F3)),
                    shape = RoundedCornerShape(12.dp),
                    border = borderStrokeHelper()
                ) {
                    Box(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Mantenha sua lista de alergias atualizada para garantir sua segurança em situações de emergência.",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = Secondary,
                                lineHeight = 20.sp
                            )
                        )
                    }
                }
            }

            // Excluir Actions button
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (selectedToDelete.isNotEmpty()) {
                        Button(
                            onClick = { viewModel.deleteSelectedAllergies() },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.error.copy(alpha = 0.1f),
                                contentColor = MaterialTheme.colorScheme.error
                            ),
                            border = borderStrokeHelper(color = MaterialTheme.colorScheme.error.copy(alpha = 0.3f)),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                            modifier = Modifier.testTag("allergies_btn_excluir")
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

            // Allergies list
            if (allergies.isEmpty()) {
                item {
                    EmptyStatePlaceholder(text = "Nenhuma alergia adicionada.")
                }
            } else {
                items(allergies) { allergy ->
                    AllergyCardItem(
                        allergy = allergy,
                        isSelected = selectedToDelete.contains(allergy.id),
                        onCheckedChange = { viewModel.toggleAllergySelection(allergy.id) },
                        onClick = { viewModel.selectAllergy(allergy) }
                    )
                }
            }

            // Add New Allergy Button at bottom of scroll list
            item {
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(
                        onClick = onNavigateToAddAllergy,
                        shape = CircleShape,
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryContainer),
                        modifier = Modifier
                            .height(48.dp)
                            .testTag("btn_add_new_allergy")
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier.padding(horizontal = 12.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Add Icon",
                                tint = Color.White
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Adicionar Nova Alergia",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            }
        }

        // Details Modal
        if (isAllergyModalVisible && selectedAllergy != null) {
            AllergyDetailsModal(
                allergy = selectedAllergy!!,
                onDismiss = { viewModel.closeAllergyModal() }
            )
        }
    }
}

@Composable
fun AllergyCardItem(
    allergy: Allergy,
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
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.weight(1f)
            ) {
                Checkbox(
                    checked = isSelected,
                    onCheckedChange = { onCheckedChange() },
                    colors = CheckboxDefaults.colors(checkedColor = Primary)
                )

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
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(iconBg),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = "Allergy icon",
                        tint = iconColor,
                        modifier = Modifier.size(22.dp)
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
                            color = if (allergy.severity == "Grave" || allergy.severity == "Medicamentosa") {
                                MaterialTheme.colorScheme.error
                            } else {
                                Secondary
                            },
                            fontWeight = if (allergy.severity == "Grave" || allergy.severity == "Medicamentosa") {
                                FontWeight.Bold
                            } else {
                                FontWeight.Normal
                            }
                        )
                    )
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "Ver Detalhes",
                    fontSize = 11.sp,
                    color = Primary,
                    fontWeight = FontWeight.Bold
                )
                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = "chevron",
                    tint = Secondary
                )
            }
        }
    }
}

@Composable
fun borderStrokeHelper() = BorderStroke(1.dp, Color(0xFFBDC9C8).copy(alpha = 0.4f))
