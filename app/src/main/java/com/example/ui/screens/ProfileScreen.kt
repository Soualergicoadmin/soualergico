package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.UserProfile
import com.example.ui.theme.Primary
import com.example.ui.theme.PrimaryContainer
import com.example.ui.theme.Secondary
import com.example.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(viewModel: MainViewModel) {
    val profile by viewModel.profile.collectAsState()

    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var state by remember { mutableStateOf("SP") }
    var city by remember { mutableStateOf("São Paulo") }

    var contact1Name by remember { mutableStateOf("") }
    var contact1Phone by remember { mutableStateOf("") }
    var contact2Name by remember { mutableStateOf("") }
    var contact2Phone by remember { mutableStateOf("") }

    // Dropdown state variables
    var isStateExpanded by remember { mutableStateOf(false) }
    var isCityExpanded by remember { mutableStateOf(false) }

    // Populate initial values when profile loads
    LaunchedEffect(profile) {
        name = profile.name
        email = profile.email
        phone = profile.phone
        address = profile.address
        state = profile.state
        city = profile.city
        contact1Name = profile.contact1Name
        contact1Phone = profile.contact1Phone
        contact2Name = profile.contact2Name
        contact2Phone = profile.contact2Phone
    }

    val scrollState = rememberScrollState()

    val states = listOf("SP", "RJ", "MG", "BA", "PR", "RS")
    val cities = when (state) {
        "SP" -> listOf("São Paulo", "Guarulhos", "Campinas", "Osasco", "Santo André")
        "RJ" -> listOf("Rio de Janeiro", "Niterói", "Duque de Caxias", "São Gonçalo")
        "MG" -> listOf("Belo Horizonte", "Uberlândia", "Contagem", "Juiz de Fora")
        "BA" -> listOf("Salvador", "Feira de Santana", "Camaçari")
        "PR" -> listOf("Curitiba", "Londrina", "Maringá")
        else -> listOf("Outra Cidade")
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF6FAF9))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 80.dp) // Height for Bottom Navigation
        ) {
            // Top App Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    IconButton(onClick = { /* Menu */ }) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Menu Icon",
                            tint = Primary
                        )
                    }
                    Text(
                        text = "Perfil",
                        style = MaterialTheme.typography.titleLarge.copy(
                            color = Primary,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }

                // Friendly Doctor Image (generated) shown on the right
                Image(
                    painter = painterResource(id = R.drawable.img_doctor_avatar),
                    contentDescription = "Doctor Assistant Avatar",
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .border(1.dp, Color(0xFFBDC9C8), CircleShape),
                    contentScale = ContentScale.Crop
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
                    .verticalScroll(scrollState),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                // Profile Avatar Photo edit circle header
                Box(
                    modifier = Modifier.padding(top = 8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFEBEFEE))
                            .border(3.dp, Color.White, CircleShape)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.img_profile_avatar),
                            contentDescription = "Profile Photo",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }

                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(Primary)
                            .align(Alignment.BottomEnd)
                            .border(1.5.dp, Color.White, CircleShape)
                            .clickable { /* Edit profile pic */ },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Editar Foto",
                            tint = Color.White,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }

                Text(
                    text = name,
                    style = MaterialTheme.typography.headlineMedium.copy(
                        color = Color(0xFF181C1C),
                        fontWeight = FontWeight.Bold
                    )
                )

                // Form section: INFORMAÇÕES DA CONTA
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        "INFORMAÇÕES DA CONTA",
                        style = MaterialTheme.typography.labelLarge.copy(
                            color = Primary,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                    )

                    // Email input
                    ProfileField(
                        label = "E-mail",
                        value = email,
                        onValueChange = { email = it },
                        icon = Icons.Default.Mail
                    )

                    // Phone input
                    ProfileField(
                        label = "Telefone (WhatsApp)",
                        value = phone,
                        onValueChange = { phone = it },
                        icon = Icons.Default.Call
                    )

                    // Address input
                    ProfileField(
                        label = "Endereço",
                        value = address,
                        onValueChange = { address = it },
                        icon = Icons.Default.LocationOn
                    )

                    // State UF Selector dropdown
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(
                            text = "Estado (UF)",
                            style = MaterialTheme.typography.labelMedium.copy(color = Secondary)
                        )
                        ExposedDropdownMenuBox(
                            expanded = isStateExpanded,
                            onExpandedChange = { isStateExpanded = !isStateExpanded }
                        ) {
                            OutlinedTextField(
                                value = state,
                                onValueChange = {},
                                readOnly = true,
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.Map,
                                        contentDescription = "State"
                                    )
                                },
                                trailingIcon = {
                                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = isStateExpanded)
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .menuAnchor(),
                                shape = RoundedCornerShape(12.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedContainerColor = Color(0xFFF0F4F3),
                                    unfocusedContainerColor = Color(0xFFF0F4F3)
                                )
                            )
                            ExposedDropdownMenu(
                                expanded = isStateExpanded,
                                onDismissRequest = { isStateExpanded = false }
                            ) {
                                states.forEach { s ->
                                    DropdownMenuItem(
                                        text = { Text(s) },
                                        onClick = {
                                            state = s
                                            isStateExpanded = false
                                            // Reset city if state changes
                                            city = when (s) {
                                                "SP" -> "São Paulo"
                                                "RJ" -> "Rio de Janeiro"
                                                "MG" -> "Belo Horizonte"
                                                "BA" -> "Salvador"
                                                "PR" -> "Curitiba"
                                                else -> "Outra Cidade"
                                            }
                                        }
                                    )
                                }
                            }
                        }
                    }

                    // City Selector dropdown
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(
                            text = "Cidade",
                            style = MaterialTheme.typography.labelMedium.copy(color = Secondary)
                        )
                        ExposedDropdownMenuBox(
                            expanded = isCityExpanded,
                            onExpandedChange = { isCityExpanded = !isCityExpanded }
                        ) {
                            OutlinedTextField(
                                value = city,
                                onValueChange = {},
                                readOnly = true,
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.LocationCity,
                                        contentDescription = "City"
                                    )
                                },
                                trailingIcon = {
                                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = isCityExpanded)
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .menuAnchor(),
                                shape = RoundedCornerShape(12.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedContainerColor = Color(0xFFF0F4F3),
                                    unfocusedContainerColor = Color(0xFFF0F4F3)
                                )
                            )
                            ExposedDropdownMenu(
                                expanded = isCityExpanded,
                                onDismissRequest = { isCityExpanded = false }
                            ) {
                                cities.forEach { c ->
                                    DropdownMenuItem(
                                        text = { Text(c) },
                                        onClick = {
                                            city = c
                                            isCityExpanded = false
                                        }
                                    )
                                }
                            }
                        }
                    }
                }

                // Section: CONTATOS DE EMERGÊNCIA
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "CONTATOS DE EMERGÊNCIA",
                            style = MaterialTheme.typography.labelLarge.copy(
                                color = Primary,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            )
                        )
                        Icon(
                            imageVector = Icons.Default.Emergency,
                            contentDescription = "Asterisk warning",
                            tint = MaterialTheme.colorScheme.error,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    // Contact 1 Container Card
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF0F4F3)),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth(),
                        border = borderStrokeHelper()
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            ProfileField(
                                label = "Nome do Contato 1",
                                value = contact1Name,
                                onValueChange = { contact1Name = it },
                                containerColor = Color.White
                            )

                            ProfileField(
                                label = "Telefone do Contato 1",
                                value = contact1Phone,
                                onValueChange = { contact1Phone = it },
                                containerColor = Color.White
                            )
                        }
                    }

                    // Contact 2 Container Card
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF0F4F3)),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth(),
                        border = borderStrokeHelper()
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            ProfileField(
                                label = "Nome do Contato 2",
                                value = contact2Name,
                                onValueChange = { contact2Name = it },
                                containerColor = Color.White
                            )

                            ProfileField(
                                label = "Telefone do Contato 2",
                                value = contact2Phone,
                                onValueChange = { contact2Phone = it },
                                containerColor = Color.White
                            )
                        }
                    }
                }

                // Save Profile action button
                Button(
                    onClick = {
                        viewModel.saveProfile(
                            UserProfile(
                                name = name,
                                email = email,
                                phone = phone,
                                address = address,
                                state = state,
                                city = city,
                                contact1Name = contact1Name,
                                contact1Phone = contact1Phone,
                                contact2Name = contact2Name,
                                contact2Phone = contact2Phone
                            )
                        )
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryContainer),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .padding(bottom = 8.dp)
                        .testTag("profile_btn_save")
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
                            "Salvar Alterações",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
fun ProfileField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    icon: androidx.compose.ui.graphics.vector.ImageVector? = null,
    containerColor: Color = Color(0xFFF0F4F3)
) {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium.copy(color = Secondary)
        )
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            leadingIcon = if (icon != null) {
                {
                    Icon(
                        imageVector = icon,
                        contentDescription = label,
                        tint = Secondary
                    )
                }
            } else null,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = containerColor,
                unfocusedContainerColor = containerColor,
                focusedIndicatorColor = PrimaryContainer,
                unfocusedIndicatorColor = Color.Transparent
            )
        )
    }
}
