package com.example.ui

import android.content.Context
import android.content.Intent
import androidx.core.content.FileProvider
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import android.net.Uri
import android.graphics.BitmapFactory
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import android.graphics.Bitmap
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec
import android.util.Base64
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.layout.ContentScale
import com.example.R
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.*
import com.example.ui.theme.*
import com.example.ui.viewmodel.AllergyViewModel
import kotlin.random.Random

// --- COLORS ---
val MedicalBlue = Color(0xFF0284C7)
val MedicalBlueLight = Color(0xFFE0F2FE)
val EmergencyRed = Color(0xFFEF4444)
val EmergencyRedLight = Color(0xFFFEE2E2)
val SoftGrey = Color(0xFFF1F5F9)
val DarkText = Color(0xFF0F172A)
val MutedText = Color(0xFF64748B)

// --- TYPOGRAPHY ---
val ArialFontFamily = FontFamily(android.graphics.Typeface.create("Arial", android.graphics.Typeface.NORMAL))

val ArialTextStyle = TextStyle(
    fontFamily = ArialFontFamily,
    color = Color.Black,
    fontSize = 16.sp
)

@Composable
fun getBlackTextFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedTextColor = Color.Black,
    unfocusedTextColor = Color.Black,
    focusedLabelColor = Color.Black,
    unfocusedLabelColor = Color.Black,
    focusedPlaceholderColor = Color.Gray,
    unfocusedPlaceholderColor = Color.Gray,
    focusedBorderColor = MedicalBlue,
    unfocusedBorderColor = Color(0xFFCBD5E1)
)

// --- LOCALIZATION HELPER ---
fun getLocalizedText(key: String, lang: String): String {
    val dictionary = mapOf(
        "Português (Brasil)" to mapOf(
            "title" to "Sou Alérgico",
            "subtitle" to "Sua carteira de alergias e emergências em mãos",
            "greeting" to "Olá",
            "guest" to "Usuário",
            "my_allergies" to "Minhas Alergias",
            "add_allergy" to "Cadastrar Alergia",
            "category" to "Categoria",
            "allergen" to "Alergênio",
            "symptoms" to "Sintomas",
            "prescription" to "Prescrição Médica",
            "profile" to "Perfil",
            "home" to "Início",
            "full_name" to "Nome Completo",
            "whatsapp" to "Telefone (WhatsApp)",
            "address" to "Endereço Completo",
            "blood_type" to "Tipo Sanguíneo",
            "emergency_contact" to "Contato de Emergência",
            "relationship" to "Parentesco",
            "emergency_toggle" to "Ativar Botão de Alerta na Tela de Bloqueio",
            "save" to "Salvar",
            "close" to "Fechar",
            "login" to "Entrar",
            "google_login" to "Entrar com o Google",
            "email" to "E-mail",
            "password" to "Senha",
            "attachment" to "Anexar prescrição médica",
            "allergy_form" to "Preencha os campos abaixo para cadastrar uma alergia",
            "symptoms_placeholder" to "Esta alergia causa os seguintes sintomas...",
            "prescription_placeholder" to "Meu médico indica...",
            "emergency_card" to "Ficha Médica de Emergência",
            "scannable_msg" to "SOCORRISTA: Escaneie o QR Code para acessar meus dados médicos.",
            "language" to "Idioma de Preferência",
            "required_fields" to "Por favor, preencha todos os campos obrigatórios.",
            "login_success" to "Login efetuado com sucesso!",
            "google_login_success" to "Login via Google simulado com sucesso!",
            "allergy_saved" to "Alergia cadastrada com sucesso!",
            "profile_saved" to "Perfil atualizado com sucesso!",
            "attachment_simulated" to "Prescrição anexada com sucesso!",
            "subscription_banner" to "Assinatura Mensal Premium por apenas R$ 9,90/mês. Acesse sua carteira de emergência offline, mesmo sem internet!",
            "confirm_delete_title" to "Excluir Alergia?",
            "confirm_delete_message" to "Tem certeza de que deseja excluir esta alergia?",
            "confirm_delete_yes" to "Sim, excluir",
            "confirm_delete_no" to "Cancelar"
        ),
        "English" to mapOf(
            "title" to "I'm Allergic",
            "subtitle" to "Your allergy and emergency card in your hands",
            "greeting" to "Hello",
            "guest" to "User",
            "my_allergies" to "My Allergies",
            "add_allergy" to "Register Allergy",
            "category" to "Category",
            "allergen" to "Allergen",
            "symptoms" to "Symptoms",
            "prescription" to "Medical Prescription",
            "profile" to "Profile",
            "home" to "Home",
            "full_name" to "Full Name",
            "whatsapp" to "Phone (WhatsApp)",
            "address" to "Full Address",
            "blood_type" to "Blood Type",
            "emergency_contact" to "Emergency Contact",
            "relationship" to "Relationship",
            "emergency_toggle" to "Enable Emergency Button on Lock Screen",
            "save" to "Save",
            "close" to "Close",
            "login" to "Login",
            "google_login" to "Login with Google",
            "email" to "Email",
            "password" to "Password",
            "attachment" to "Attach medical prescription",
            "allergy_form" to "Fill in the fields below to register an allergy",
            "symptoms_placeholder" to "This allergy causes the following symptoms...",
            "prescription_placeholder" to "My doctor recommends...",
            "emergency_card" to "Emergency Medical Card",
            "scannable_msg" to "RESPONDER: Scan QR Code to access my medical data.",
            "language" to "Preferred Language",
            "required_fields" to "Please fill in all required fields.",
            "login_success" to "Successfully logged in!",
            "google_login_success" to "Google Sign-In successfully simulated!",
            "allergy_saved" to "Allergy registered successfully!",
            "profile_saved" to "Profile updated successfully!",
            "attachment_simulated" to "Prescription attached successfully!",
            "subscription_banner" to "Premium Monthly Subscription for only $1.99/mo. Access your emergency profile offline, anywhere, anytime!",
            "confirm_delete_title" to "Delete Allergy?",
            "confirm_delete_message" to "Are you sure you want to delete this allergy?",
            "confirm_delete_yes" to "Yes, delete",
            "confirm_delete_no" to "Cancel"
        ),
        "Español" to mapOf(
            "title" to "Soy Alérgico",
            "subtitle" to "Tu tarjeta de alergia y emergencia en tus manos",
            "greeting" to "Hola",
            "guest" to "Usuario",
            "my_allergies" to "Mis Alergias",
            "add_allergy" to "Registrar Alergia",
            "category" to "Categoría",
            "allergen" to "Alérgeno",
            "symptoms" to "Síntomas",
            "prescription" to "Receta Médica",
            "profile" to "Perfil",
            "home" to "Inicio",
            "full_name" to "Nombre Completo",
            "whatsapp" to "Teléfono (WhatsApp)",
            "address" to "Dirección Completa",
            "blood_type" to "Tipo de Sangre",
            "emergency_contact" to "Contacto de Emergencia",
            "relationship" to "Parentesco",
            "emergency_toggle" to "Activar Botón de Alerta en Pantalla de Bloqueio",
            "save" to "Guardar",
            "close" to "Cerrar",
            "login" to "Iniciar sesión",
            "google_login" to "Iniciar sesión con Google",
            "email" to "Correo electrónico",
            "password" to "Contraseña",
            "attachment" to "Adjuntar receta médica",
            "allergy_form" to "Complete los campos para registrar una alergia",
            "symptoms_placeholder" to "Esta alergia causa los siguientes síntomas...",
            "prescription_placeholder" to "Mi médico indica...",
            "emergency_card" to "Ficha Médica de Emergencia",
            "scannable_msg" to "SOCORRISTA: Escanee el código QR para acceder a mis datos médicos.",
            "language" to "Idioma de Preferencia",
            "required_fields" to "Por favor complete los campos requeridos.",
            "login_success" to "¡Inicio de sesión exitoso!",
            "google_login_success" to "¡Simulación de inicio de sesión con Google exitosa!",
            "allergy_saved" to "¡Alergia registrada con éxito!",
            "profile_saved" to "¡Perfil actualizado con éxito!",
            "attachment_simulated" to "¡Receta médica adjuntada con éxito!",
            "subscription_banner" to "Suscripción Premium Mensual por solo $1.99/mes. ¡Accede a tu ficha médica de emergencia offline, en cualquier lugar!",
            "confirm_delete_title" to "¿Eliminar Alergia?",
            "confirm_delete_message" to "¿Está seguro de que desea eliminar esta alergia?",
            "confirm_delete_yes" to "Sí, eliminar",
            "confirm_delete_no" to "Cancelar"
        )
    )
    val langMap = dictionary[lang] ?: dictionary["Português (Brasil)"]!!
    return langMap[key] ?: key
}

// Helper to compress image to max 50KB
fun compressImageToLimit(context: Context, uri: Uri, limitKb: Long = 50): Uri? {
    try {
        val inputStream = context.contentResolver.openInputStream(uri) ?: return null
        val bitmap = BitmapFactory.decodeStream(inputStream)
        var quality = 100
        var outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
        
        while (outputStream.toByteArray().size / 1024 > limitKb && quality > 10) {
            quality -= 10
            outputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
        }
        
        val tempFile = File(context.cacheDir, "compressed_${System.currentTimeMillis()}.jpg")
        FileOutputStream(tempFile).use { it.write(outputStream.toByteArray()) }
        return Uri.fromFile(tempFile)
    } catch (e: Exception) {
        return null
    }
}

fun saveUriToInternalStorage(context: Context, uri: Uri): String? {
    try {
        val fileName = "prescription_${System.currentTimeMillis()}"
        val contentResolver = context.contentResolver
        val mimeType = contentResolver.getType(uri)
        val extension = if (mimeType == "application/pdf") "pdf" else "jpg"
        val file = File(context.filesDir, "$fileName.$extension")
        
        contentResolver.openInputStream(uri)?.use { input ->
            FileOutputStream(file).use { output ->
                input.copyTo(output)
            }
        }
        return file.absolutePath
    } catch (e: Exception) {
        return null
    }
}

// Função para criptografar o texto das alergias usando um PIN de 4 dígitos
fun encryptEmergencyData(plainText: String, pinCode: String): String {
    // Ajusta a chave para ter 16 bytes (AES-128)
    val keyBytes = pinCode.padEnd(16, '0').toByteArray(Charsets.UTF_8)
    val secretKey = SecretKeySpec(keyBytes, "AES")
    
    val cipher = Cipher.getInstance("AES/ECB/PKCS5Padding")
    cipher.init(Cipher.ENCRYPT_MODE, secretKey)
    
    val encryptedBytes = cipher.doFinal(plainText.toByteArray(Charsets.UTF_8))
    
    // Retorna a string codificada para URL
    return Base64.encodeToString(encryptedBytes, Base64.URL_SAFE or Base64.NO_WRAP)
}

// --- PHONE INPUT MASK UTILITY ---
fun formatWhatsAppNumber(raw: String): String {
    val digits = raw.filter { it.isDigit() }
    return when {
        digits.length <= 2 -> digits
        digits.length <= 7 -> "(${digits.substring(0, 2)}) ${digits.substring(2)}"
        digits.length <= 11 -> "(${digits.substring(0, 2)}) ${digits.substring(2, 7)}-${digits.substring(7)}"
        else -> "(${digits.substring(0, 2)}) ${digits.substring(2, 7)}-${digits.substring(7, 11)}"
    }
}

// --- MAIN COMPOSE FILE ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SafeMyAlertsApp(viewModel: AllergyViewModel) {
    val context = LocalContext.current
    val dbProfile by viewModel.userProfile.collectAsStateWithLifecycle()
    val dbAllergies by viewModel.allergies.collectAsStateWithLifecycle()
    val dbContacts by viewModel.contacts.collectAsStateWithLifecycle()

    // Authentication States
    var isLoggedIn by remember { mutableStateOf(false) }
    var emailInput by remember { mutableStateOf("") }
    var passwordInput by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }

    // Navigation state: 0 = Home/Dashboard ("Início"), 1 = Profile ("Perfil")
    var selectedTab by remember { mutableIntStateOf(0) }

    // Screen states
    var activeAllergyDetail by remember { mutableStateOf<Allergy?>(null) }
    var isAddingAllergy by remember { mutableStateOf(false) }
    var isEmergencyCardOpen by remember { mutableStateOf(false) }
    var showQrCodeOnly by remember { mutableStateOf(false) }
    var isQRCodeOpen by remember { mutableStateOf(false) }
    var allergyToDelete by remember { mutableStateOf<Allergy?>(null) }
    
    // Preferences for emergency card display
    var showName by remember { mutableStateOf(true) }
    var showContact by remember { mutableStateOf(true) }
    var showBlood by remember { mutableStateOf(true) }
    var showAllergies by remember { mutableStateOf(true) }
    var showQRCode by remember { mutableStateOf(true) }

    // Profile Screen States (locally buffered and written back on Save)
    var profileName by remember { mutableStateOf("") }
    var profilePhone by remember { mutableStateOf("") }
    var profileAddress by remember { mutableStateOf("") }
    var profileAddressStreet by remember { mutableStateOf("") }
    var profileAddressNumber by remember { mutableStateOf("") }
    var profileAddressNeigh by remember { mutableStateOf("") }
    var profileAddressCity by remember { mutableStateOf("") }
    var profileAddressState by remember { mutableStateOf("") }
    var profileBloodType by remember { mutableStateOf("O+") }
    var profileContactName by remember { mutableStateOf("") }
    var profileContactPhone by remember { mutableStateOf("") }
    var profileContactRelation by remember { mutableStateOf("") }
    var profileIsAlertButtonEnabled by remember { mutableStateOf(true) }
    var profileLanguage by remember { mutableStateOf("Português (Brasil)") }

    // Localized language reference for screens (instantly reactive)
    val currentLang = if (isLoggedIn) profileLanguage else "Português (Brasil)"

    // Load states whenever DB profile updates
    LaunchedEffect(dbProfile) {
        dbProfile?.let {
            profileName = it.name
            profilePhone = it.phone
            profileAddress = it.address
            val parts = it.address.split(" | ")
            if (parts.size >= 5) {
                profileAddressStreet = parts[0]
                profileAddressNumber = parts[1]
                profileAddressNeigh = parts[2]
                profileAddressCity = parts[3]
                profileAddressState = parts[4]
            } else {
                profileAddressStreet = it.address
                profileAddressNumber = ""
                profileAddressNeigh = ""
                profileAddressCity = ""
                profileAddressState = ""
            }
            profileBloodType = if (it.bloodType.isNotBlank()) it.bloodType else "O+"
            profileContactName = it.emergencyContactName
            profileContactPhone = it.emergencyContactPhone
            profileContactRelation = it.emergencyContactRelation
            profileIsAlertButtonEnabled = it.isAlertButtonEnabled
            profileLanguage = if (it.preferredLanguage.isNotBlank()) it.preferredLanguage else "Português (Brasil)"
        }
    }

    // Floating SOS Pulse Button pulse animation values (concentric CSS pulse effect)
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseScale1 by infiniteTransition.animateFloat(
        initialValue = 1.0f,
        targetValue = 1.6f,
        animationSpec = infiniteRepeatable(
            animation = tween(1400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "pulseScale1"
    )
    val pulseAlpha1 by infiniteTransition.animateFloat(
        initialValue = 0.6f,
        targetValue = 0.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "pulseAlpha1"
    )

    val pulseScale2 by infiniteTransition.animateFloat(
        initialValue = 1.0f,
        targetValue = 1.3f,
        animationSpec = infiniteRepeatable(
            animation = tween(1400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "pulseScale2"
    )
    val pulseAlpha2 by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 0.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "pulseAlpha2"
    )

    if (!isLoggedIn) {
        // --- AUTHENTICATION SCREEN ---
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .systemBarsPadding(),
            contentAlignment = Alignment.Center
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
                    .widthIn(max = 500.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    // Medical-themed minimalist vector logo (replaced with high-resolution img_logo, enlarged 2x)
                    Image(
                        painter = painterResource(id = R.drawable.img_logo),
                        contentDescription = "Logo Sou Alérgico",
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .size(180.dp)
                            .clip(RoundedCornerShape(16.dp))
                    )
                }

                item {
                    Text(
                        text = "Sou Alérgico",
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 32.sp,
                        color = DarkText,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = getLocalizedText("subtitle", currentLang),
                        fontSize = 14.sp,
                        color = MutedText,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(top = 4.dp, bottom = 12.dp)
                    )
                }

                item {
                    // Email Field
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = getLocalizedText("email", currentLang),
                            fontFamily = ArialFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = DarkText
                        )
                        OutlinedTextField(
                            value = emailInput,
                            onValueChange = {
                                emailInput = it
                                emailError = null
                            },
                            placeholder = { Text("exemplo@email.com", fontFamily = ArialFontFamily, color = Color.Gray) },
                            isError = emailError != null,
                            supportingText = emailError?.let { { Text(it) } },
                            leadingIcon = { Icon(Icons.Default.Email, contentDescription = null, tint = MedicalBlue) },
                            textStyle = ArialTextStyle,
                            colors = getBlackTextFieldColors(),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("email_input"),
                            shape = RoundedCornerShape(12.dp),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                        )
                    }
                }

                item {
                    // Password Field
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = getLocalizedText("password", currentLang),
                            fontFamily = ArialFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = DarkText
                        )
                        OutlinedTextField(
                            value = passwordInput,
                            onValueChange = {
                                passwordInput = it
                                passwordError = null
                            },
                            isError = passwordError != null,
                            supportingText = passwordError?.let { { Text(it) } },
                            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = MedicalBlue) },
                            textStyle = ArialTextStyle,
                            colors = getBlackTextFieldColors(),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("password_input"),
                            shape = RoundedCornerShape(12.dp),
                            singleLine = true,
                            visualTransformation = PasswordVisualTransformation(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
                        )
                    }
                }

                item {
                    // Sign-In Button
                    Button(
                        onClick = {
                            // Validation
                            var valid = true
                            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
                                emailError = "E-mail inválido"
                                valid = false
                            }
                            if (passwordInput.length < 6) {
                                passwordError = "A senha deve ter pelo menos 6 caracteres"
                                valid = false
                            }
                            if (valid) {
                                isLoggedIn = true
                                Toast.makeText(context, getLocalizedText("login_success", currentLang), Toast.LENGTH_SHORT).show()
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = MedicalBlue),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .testTag("login_button")
                    ) {
                        Text(
                            text = getLocalizedText("login", currentLang),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }

                item {
                    // Google Sign-In button
                    OutlinedButton(
                        onClick = {
                            isLoggedIn = true
                            Toast.makeText(context, getLocalizedText("google_login_success", currentLang), Toast.LENGTH_SHORT).show()
                        },
                        border = BorderStroke(1.dp, Color(0xFFCBD5E1)),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .testTag("google_login_button")
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            // Custom vector rendering of Google G letter representation
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .padding(end = 8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Share,
                                    contentDescription = "Google Icon",
                                    tint = Color(0xFFEA4335),
                                    modifier = Modifier.scale(0.85f)
                                )
                            }
                            Text(
                                text = getLocalizedText("google_login", currentLang),
                                fontSize = 15.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = DarkText
                            )
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    // Premium monthly subscription banner / business model hint
                    Card(
                        colors = CardDefaults.cardColors(containerColor = MedicalBlueLight),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = "Subscription Info",
                                    tint = Color(0xFFF59E0B),
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Premium",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = MedicalBlue
                                )
                            }
                            Text(
                                text = getLocalizedText("subscription_banner", currentLang),
                                fontSize = 12.sp,
                                color = DarkText,
                                lineHeight = 16.sp
                            )
                        }
                    }
                }
            }
        }
    } else if (isAddingAllergy) {
        // --- TELA DE CADASTRO DE ALERGIA (Dedicated Form View) ---
        var allergyCategory by remember { mutableStateOf("Medicamento") }
        var isCategoryDropdownOpen by remember { mutableStateOf(false) }
        var allergyNameInput by remember { mutableStateOf("") }
        var allergySymptomsInput by remember { mutableStateOf("") }
        var allergyPrescriptionInput by remember { mutableStateOf("") }
        var isAttachmentAdded by remember { mutableStateOf(false) }
        var attachmentPath by remember { mutableStateOf<String?>(null) }
        val context = LocalContext.current
        val launcher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.OpenDocument()
        ) { uri: Uri? ->
            uri?.let {
                val path = saveUriToInternalStorage(context, it)
                if (path != null) {
                    attachmentPath = path
                    isAttachmentAdded = true
                }
            }
        }

        val categories = listOf("Medicamento", "Alimentar", "Outra")

        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            text = getLocalizedText("add_allergy", currentLang),
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = DarkText
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { isAddingAllergy = false }) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Voltar", tint = DarkText)
                        }
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.White)
                )
            }
        ) { paddingValues ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(Color.White)
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item {
                    Text(
                        text = getLocalizedText("allergy_form", currentLang),
                        fontSize = 14.sp,
                        color = MutedText,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Start
                    )
                }

                // Category selection dropdown
                item {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = getLocalizedText("category", currentLang) + " *",
                            fontFamily = ArialFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = DarkText
                        )
                        Box(modifier = Modifier.fillMaxWidth()) {
                            OutlinedTextField(
                                value = allergyCategory,
                                onValueChange = {},
                                readOnly = true,
                                textStyle = ArialTextStyle,
                                colors = getBlackTextFieldColors(),
                                trailingIcon = {
                                    IconButton(onClick = { isCategoryDropdownOpen = true }) {
                                        Icon(Icons.Default.ArrowDropDown, contentDescription = "Dropdown")
                                    }
                                },
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { isCategoryDropdownOpen = true }
                                    .testTag("allergy_category_dropdown")
                            )
                            DropdownMenu(
                                expanded = isCategoryDropdownOpen,
                                onDismissRequest = { isCategoryDropdownOpen = false },
                                modifier = Modifier
                                    .fillMaxWidth(0.85f)
                                    .background(Color.White)
                            ) {
                                categories.forEach { cat ->
                                    DropdownMenuItem(
                                        text = { Text(cat, fontFamily = ArialFontFamily, color = Color.Black) },
                                        onClick = {
                                            allergyCategory = cat
                                            isCategoryDropdownOpen = false
                                        },
                                        modifier = Modifier.background(Color.White)
                                    )
                                }
                            }
                        }
                    }
                }

                // Allergen Name Input
                item {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = "Sou alérgico a: *",
                            fontFamily = ArialFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = DarkText
                        )
                        OutlinedTextField(
                            value = allergyNameInput,
                            onValueChange = { allergyNameInput = it },
                            placeholder = { Text("ex: Dipirona, Camarão, Amendoim", fontFamily = ArialFontFamily, color = Color.Gray) },
                            textStyle = ArialTextStyle,
                            colors = getBlackTextFieldColors(),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("allergy_name_input"),
                            singleLine = true
                        )
                    }
                }

                // Symptoms Textarea
                item {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = "Esta alergia causa os seguintes sintomas: *",
                            fontFamily = ArialFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = DarkText
                        )
                        OutlinedTextField(
                            value = allergySymptomsInput,
                            onValueChange = { allergySymptomsInput = it },
                            placeholder = { Text(getLocalizedText("symptoms_placeholder", currentLang), fontFamily = ArialFontFamily, color = Color.Gray) },
                            textStyle = ArialTextStyle,
                            colors = getBlackTextFieldColors(),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(110.dp)
                                .testTag("allergy_symptoms_input"),
                            maxLines = 4
                        )
                    }
                }

                // Prescription Instructions
                item {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = "Meu médico indica:",
                            fontFamily = ArialFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = DarkText
                        )
                        OutlinedTextField(
                            value = allergyPrescriptionInput,
                            onValueChange = { allergyPrescriptionInput = it },
                            placeholder = { Text(getLocalizedText("prescription_placeholder", currentLang), fontFamily = ArialFontFamily, color = Color.Gray) },
                            textStyle = ArialTextStyle,
                            colors = getBlackTextFieldColors(),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(110.dp)
                                .testTag("allergy_prescription_input"),
                            maxLines = 4
                        )

                        // File attachment
                    }
                }

                // Optional File Attachment
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (isAttachmentAdded) Color(0xFFF0FDF4) else SoftGrey)
                            .border(
                                width = 1.dp,
                                color = if (isAttachmentAdded) Color(0xFF4ADE80) else Color(0xFFE2E8F0),
                                shape = RoundedCornerShape(12.dp)
                            )
                            .clickable {
                                launcher.launch(arrayOf("image/*", "application/pdf"))
                            }
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = if (isAttachmentAdded) Icons.Default.CheckCircle else Icons.Default.Add,
                                contentDescription = null,
                                tint = if (isAttachmentAdded) Color(0xFF16A34A) else MedicalBlue,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (isAttachmentAdded) "Prescrição_Médica ✔" else getLocalizedText("attachment", currentLang),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isAttachmentAdded) Color(0xFF16A34A) else MedicalBlue
                            )
                        }
                    }
                }

                // Action buttons (Save)
                item {
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = {
                            if (allergyNameInput.isBlank() || allergySymptomsInput.isBlank()) {
                                Toast.makeText(context, getLocalizedText("required_fields", currentLang), Toast.LENGTH_SHORT).show()
                            } else {
                                viewModel.addAllergy(
                                    allergen = allergyNameInput,
                                    severity = allergyCategory,
                                    symptoms = allergySymptomsInput,
                                    rescueMedication = allergyPrescriptionInput,
                                    prescriptionFilePath = attachmentPath // Pass the path!
                                )
                                isAddingAllergy = false
                                Toast.makeText(context, getLocalizedText("allergy_saved", currentLang), Toast.LENGTH_SHORT).show()
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = MedicalBlue),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp)
                            .testTag("save_allergy_button")
                    ) {
                        Text(
                            text = getLocalizedText("save", currentLang),
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = Color.White
                        )
                    }
                }
            }
        }
    } else {
        // --- LOGGED-IN SYSTEM (DASHBOARD & PROFILE SCREENS) ---
        Scaffold(
            topBar = {
                // Global Header: Center-aligned brand logo with "Sou Alérgico" directly below it
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                        .padding(top = 12.dp, bottom = 8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.img_logo),
                        contentDescription = "Logo Sou Alérgico",
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .size(64.dp)
                            .clip(RoundedCornerShape(8.dp))
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Sou Alérgico",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = DarkText
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = getLocalizedText("subtitle", currentLang),
                        fontFamily = ArialFontFamily,
                        fontWeight = FontWeight.Normal,
                        fontSize = 12.sp,
                        color = MutedText,
                        textAlign = TextAlign.Center
                    )
                }
            },
            bottomBar = {
                // Bottom bar configured strictly with 2 tabs: Início & Perfil
                NavigationBar(
                    containerColor = Color.White,
                    tonalElevation = 8.dp,
                    modifier = Modifier.windowInsetsPadding(WindowInsets.navigationBars)
                ) {
                    NavigationBarItem(
                        selected = selectedTab == 0,
                        onClick = { selectedTab = 0 },
                        icon = { Icon(Icons.Default.Home, contentDescription = "Início") },
                        label = { Text(getLocalizedText("home", currentLang), fontWeight = FontWeight.Bold) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MedicalBlue,
                            selectedTextColor = MedicalBlue,
                            unselectedIconColor = MutedText,
                            unselectedTextColor = MutedText,
                            indicatorColor = MedicalBlueLight
                        )
                    )
                    NavigationBarItem(
                        selected = selectedTab == 1,
                        onClick = { selectedTab = 1 },
                        icon = { Icon(Icons.Default.Person, contentDescription = "Perfil") },
                        label = { Text(getLocalizedText("profile", currentLang), fontWeight = FontWeight.Bold) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MedicalBlue,
                            selectedTextColor = MedicalBlue,
                            unselectedIconColor = MutedText,
                            unselectedTextColor = MutedText,
                            indicatorColor = MedicalBlueLight
                        )
                    )
                }
            }
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(SoftGrey)
            ) {
                if (selectedTab == 0) {
                    // --- TELA INICIAL (DASHBOARD) ---
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                    ) {
                        // Header Greeting Section
                        val displayFirstName = profileName.split(" ").firstOrNull()?.trim() ?: ""
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "${getLocalizedText("greeting", currentLang)}, ${if (displayFirstName.isNotBlank()) displayFirstName else getLocalizedText("guest", currentLang)}!",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = DarkText,
                                modifier = Modifier.weight(1f)
                            )
                        }

                        // My Allergies Subtitle / Title Section
                        Text(
                            text = getLocalizedText("my_allergies", currentLang),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = MutedText,
                            modifier = Modifier.padding(bottom = 12.dp)
                        )

                        // Allergy Grid/List container
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth()
                        ) {
                            if (dbAllergies.isEmpty()) {
                                Card(
                                    colors = CardDefaults.cardColors(containerColor = Color.White),
                                    shape = RoundedCornerShape(16.dp),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 8.dp)
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(32.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Info,
                                            contentDescription = "No allergies",
                                            tint = MutedText,
                                            modifier = Modifier.size(48.dp)
                                        )
                                        Spacer(modifier = Modifier.height(12.dp))
                                        Text(
                                            text = "Nenhuma alergia cadastrada.",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 16.sp,
                                            color = DarkText,
                                            textAlign = TextAlign.Center
                                        )
                                        Text(
                                            text = "Clique no botão abaixo para adicionar a sua primeira alergia e manter-se seguro.",
                                            fontSize = 13.sp,
                                            color = MutedText,
                                            textAlign = TextAlign.Center,
                                            modifier = Modifier.padding(top = 4.dp)
                                        )
                                    }
                                }
                            } else {
                                LazyColumn(
                                    verticalArrangement = Arrangement.spacedBy(10.dp),
                                    modifier = Modifier.fillMaxSize()
                                ) {
                                    items(dbAllergies) { allergy ->
                                        // Interactive Allergy Card
                                        Card(
                                            colors = CardDefaults.cardColors(containerColor = Color.White),
                                            shape = RoundedCornerShape(14.dp),
                                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clickable { activeAllergyDetail = allergy }
                                                .testTag("allergy_card_${allergy.allergen.lowercase()}")
                                        ) {
                                            Row(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(16.dp),
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                // Allergy icon tag based on category
                                                val catColor = when (allergy.severity) {
                                                    "Medicamento" -> Color(0xFFF43F5E)
                                                    "Alimentar" -> Color(0xFFF59E0B)
                                                    else -> Color(0xFF0EA5E9)
                                                }
                                                Box(
                                                    modifier = Modifier
                                                        .size(44.dp)
                                                        .clip(CircleShape)
                                                        .background(catColor.copy(alpha = 0.12f)),
                                                    contentAlignment = Alignment.Center
                                                ) {
                                                    Icon(
                                                        imageVector = when (allergy.severity) {
                                                            "Medicamento" -> Icons.Default.MedicalServices
                                                            "Alimentar" -> Icons.Default.Restaurant
                                                            else -> Icons.Default.Warning
                                                        },
                                                        contentDescription = null,
                                                        tint = catColor,
                                                        modifier = Modifier.size(22.dp)
                                                    )
                                                }
                                                Spacer(modifier = Modifier.width(16.dp))
                                                Column(modifier = Modifier.weight(1f)) {
                                                    Text(
                                                        text = allergy.severity.uppercase(),
                                                        fontSize = 11.sp,
                                                        fontWeight = FontWeight.Bold,
                                                        color = catColor,
                                                        letterSpacing = 1.sp
                                                    )
                                                    Spacer(modifier = Modifier.height(2.dp))
                                                    Text(
                                                        text = "Sou alérgico a ${allergy.allergen}",
                                                        fontSize = 16.sp,
                                                        fontWeight = FontWeight.ExtraBold,
                                                        color = DarkText,
                                                        maxLines = 1,
                                                        overflow = TextOverflow.Ellipsis
                                                    )
                                                    Spacer(modifier = Modifier.height(2.dp))
                                                    Text(
                                                        text = allergy.symptoms,
                                                        fontSize = 13.sp,
                                                        color = MutedText,
                                                        maxLines = 1,
                                                        overflow = TextOverflow.Ellipsis
                                                    )
                                                }
                                                IconButton(onClick = { allergyToDelete = allergy }) {
                                                    Icon(
                                                        imageVector = Icons.Default.Delete,
                                                        contentDescription = "Excluir alergia",
                                                        tint = Color(0xFFFDA4AF)
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        // Call to Action: "Cadastrar Alergia" right above bottom navigation menu
                        Button(
                            onClick = { isAddingAllergy = true },
                            colors = ButtonDefaults.buttonColors(containerColor = MedicalBlue),
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                                .height(56.dp)
                                .testTag("btn_cadastrar_alergia")
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Add, contentDescription = null, tint = Color.White)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = getLocalizedText("add_allergy", currentLang),
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                        }
                    }
                } else {
                    // --- TELA DE PERFIL (USER PROFILE) ---
                    val arialTextStyle = TextStyle(
                        fontFamily = ArialFontFamily,
                        color = Color.Black,
                        fontSize = 16.sp
                    )
                    val blackTextFieldColors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black,
                        focusedLabelColor = Color.Black,
                        unfocusedLabelColor = Color.Black,
                        focusedPlaceholderColor = Color.Gray,
                        unfocusedPlaceholderColor = Color.Gray,
                        focusedBorderColor = MedicalBlue,
                        unfocusedBorderColor = Color(0xFFCBD5E1)
                    )

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Personal Information Card
                        item {
                            Card(
                                colors = CardDefaults.cardColors(containerColor = Color.White),
                                shape = RoundedCornerShape(16.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(
                                    modifier = Modifier.padding(16.dp),
                                    verticalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(Icons.Default.Person, contentDescription = null, tint = MedicalBlue)
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = "Informações Pessoais",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 16.sp,
                                            color = DarkText
                                        )
                                    }
                                    Divider()

                                    // Full Name
                                    Column(
                                        modifier = Modifier.fillMaxWidth(),
                                        verticalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        Text(
                                            text = getLocalizedText("full_name", currentLang),
                                            fontFamily = ArialFontFamily,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 13.sp,
                                            color = DarkText
                                        )
                                        OutlinedTextField(
                                            value = profileName,
                                            onValueChange = { profileName = it },
                                            textStyle = arialTextStyle,
                                            colors = blackTextFieldColors,
                                            shape = RoundedCornerShape(10.dp),
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .testTag("profile_name_input"),
                                            singleLine = true
                                        )
                                    }

                                    // WhatsApp (with masking)
                                    Column(
                                        modifier = Modifier.fillMaxWidth(),
                                        verticalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        Text(
                                            text = getLocalizedText("whatsapp", currentLang),
                                            fontFamily = ArialFontFamily,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 13.sp,
                                            color = DarkText
                                        )
                                        OutlinedTextField(
                                            value = profilePhone,
                                            onValueChange = { profilePhone = formatWhatsAppNumber(it) },
                                            placeholder = { Text("(11) 99999-9999", fontFamily = ArialFontFamily, color = Color.Gray) },
                                            textStyle = arialTextStyle,
                                            colors = blackTextFieldColors,
                                            shape = RoundedCornerShape(10.dp),
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .testTag("profile_phone_input"),
                                            singleLine = true,
                                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
                                        )
                                    }

                                    // Address Section Header / Split Fields Area
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "Endereço Residencial",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp,
                                        color = DarkText,
                                        fontFamily = ArialFontFamily
                                    )

                                    // Field 1: Endereço (Street)
                                    Column(
                                        modifier = Modifier.fillMaxWidth(),
                                        verticalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        Text(
                                            text = "Endereço:",
                                            fontFamily = ArialFontFamily,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 13.sp,
                                            color = DarkText
                                        )
                                        OutlinedTextField(
                                            value = profileAddressStreet,
                                            onValueChange = { profileAddressStreet = it },
                                            placeholder = { Text("Rua, Avenida, etc.", fontFamily = ArialFontFamily, color = Color.Gray) },
                                            textStyle = arialTextStyle,
                                            colors = blackTextFieldColors,
                                            shape = RoundedCornerShape(10.dp),
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .testTag("profile_address_street_input"),
                                            singleLine = true
                                        )
                                    }

                                    // Row 2: Número & Bairro
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Column(
                                            modifier = Modifier.weight(0.35f),
                                            verticalArrangement = Arrangement.spacedBy(4.dp)
                                        ) {
                                            Text(
                                                text = "Número:",
                                                fontFamily = ArialFontFamily,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 13.sp,
                                                color = DarkText
                                            )
                                            OutlinedTextField(
                                                value = profileAddressNumber,
                                                onValueChange = { profileAddressNumber = it },
                                                placeholder = { Text("ex: 123", fontFamily = ArialFontFamily, color = Color.Gray) },
                                                textStyle = arialTextStyle,
                                                colors = blackTextFieldColors,
                                                shape = RoundedCornerShape(10.dp),
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .testTag("profile_address_number_input"),
                                                singleLine = true
                                            )
                                        }

                                        Column(
                                            modifier = Modifier.weight(0.65f),
                                            verticalArrangement = Arrangement.spacedBy(4.dp)
                                        ) {
                                            Text(
                                                text = "Bairro:",
                                                fontFamily = ArialFontFamily,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 13.sp,
                                                color = DarkText
                                            )
                                            OutlinedTextField(
                                                value = profileAddressNeigh,
                                                onValueChange = { profileAddressNeigh = it },
                                                placeholder = { Text("Bairro", fontFamily = ArialFontFamily, color = Color.Gray) },
                                                textStyle = arialTextStyle,
                                                colors = blackTextFieldColors,
                                                shape = RoundedCornerShape(10.dp),
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .testTag("profile_address_neighbourhood_input"),
                                                singleLine = true
                                            )
                                        }
                                    }

                                    // Row 3: Cidade & Estado
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Column(
                                            modifier = Modifier.weight(0.65f),
                                            verticalArrangement = Arrangement.spacedBy(4.dp)
                                        ) {
                                            Text(
                                                text = "Cidade:",
                                                fontFamily = ArialFontFamily,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 13.sp,
                                                color = DarkText
                                            )
                                            OutlinedTextField(
                                                value = profileAddressCity,
                                                onValueChange = { profileAddressCity = it },
                                                placeholder = { Text("Cidade", fontFamily = ArialFontFamily, color = Color.Gray) },
                                                textStyle = arialTextStyle,
                                                colors = blackTextFieldColors,
                                                shape = RoundedCornerShape(10.dp),
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .testTag("profile_address_city_input"),
                                                singleLine = true
                                            )
                                        }

                                        Column(
                                            modifier = Modifier.weight(0.35f),
                                            verticalArrangement = Arrangement.spacedBy(4.dp)
                                        ) {
                                            Text(
                                                text = "Estado:",
                                                fontFamily = ArialFontFamily,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 13.sp,
                                                color = DarkText
                                            )
                                            OutlinedTextField(
                                                value = profileAddressState,
                                                onValueChange = { profileAddressState = it },
                                                placeholder = { Text("UF", fontFamily = ArialFontFamily, color = Color.Gray) },
                                                textStyle = arialTextStyle,
                                                colors = blackTextFieldColors,
                                                shape = RoundedCornerShape(10.dp),
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .testTag("profile_address_state_input"),
                                                singleLine = true
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        // Medical Information Card
                        item {
                            Card(
                                colors = CardDefaults.cardColors(containerColor = Color.White),
                                shape = RoundedCornerShape(16.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(
                                    modifier = Modifier.padding(16.dp),
                                    verticalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(Icons.Default.Favorite, contentDescription = null, tint = EmergencyRed)
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = "Informações Médicas",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 16.sp,
                                            color = DarkText
                                        )
                                    }
                                    Divider()

                                    // Blood Type selection dropdown
                                    val bloodTypes = listOf("A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-")
                                    var isBloodDropdownOpen by remember { mutableStateOf(false) }

                                    Column(
                                        modifier = Modifier.fillMaxWidth(),
                                        verticalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        Text(
                                            text = getLocalizedText("blood_type", currentLang),
                                            fontFamily = ArialFontFamily,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 13.sp,
                                            color = DarkText
                                        )
                                        Box(modifier = Modifier.fillMaxWidth()) {
                                            OutlinedTextField(
                                                value = profileBloodType,
                                                onValueChange = {},
                                                readOnly = true,
                                                textStyle = arialTextStyle,
                                                colors = blackTextFieldColors,
                                                trailingIcon = {
                                                    IconButton(onClick = { isBloodDropdownOpen = true }) {
                                                        Icon(Icons.Default.ArrowDropDown, contentDescription = "Dropdown")
                                                    }
                                                },
                                                shape = RoundedCornerShape(10.dp),
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .clickable { isBloodDropdownOpen = true }
                                                    .testTag("profile_blood_dropdown")
                                            )
                                            DropdownMenu(
                                                expanded = isBloodDropdownOpen,
                                                onDismissRequest = { isBloodDropdownOpen = false },
                                                modifier = Modifier
                                                    .fillMaxWidth(0.8f)
                                                    .background(Color.White)
                                            ) {
                                                bloodTypes.forEach { type ->
                                                    DropdownMenuItem(
                                                        text = { Text(type, fontFamily = ArialFontFamily, color = Color.Black) },
                                                        onClick = {
                                                            profileBloodType = type
                                                            isBloodDropdownOpen = false
                                                        },
                                                        modifier = Modifier.background(Color.White)
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        // Emergency Contact Card
                        item {
                            Card(
                                colors = CardDefaults.cardColors(containerColor = Color.White),
                                shape = RoundedCornerShape(16.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(
                                    modifier = Modifier.padding(16.dp),
                                    verticalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(Icons.Default.Phone, contentDescription = null, tint = MedicalBlue)
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = getLocalizedText("emergency_contact", currentLang),
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 16.sp,
                                            color = DarkText
                                        )
                                    }
                                    Divider()

                                    // Contact Name
                                    Column(
                                        modifier = Modifier.fillMaxWidth(),
                                        verticalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        Text(
                                            text = "Nome do Contato",
                                            fontFamily = ArialFontFamily,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 13.sp,
                                            color = DarkText
                                        )
                                        OutlinedTextField(
                                            value = profileContactName,
                                            onValueChange = { profileContactName = it },
                                            textStyle = arialTextStyle,
                                            colors = blackTextFieldColors,
                                            shape = RoundedCornerShape(10.dp),
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .testTag("profile_contact_name_input"),
                                            singleLine = true
                                        )
                                    }

                                    // Contact Phone
                                    Column(
                                        modifier = Modifier.fillMaxWidth(),
                                        verticalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        Text(
                                            text = "Telefone do Contato",
                                            fontFamily = ArialFontFamily,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 13.sp,
                                            color = DarkText
                                        )
                                        OutlinedTextField(
                                            value = profileContactPhone,
                                            onValueChange = { profileContactPhone = formatWhatsAppNumber(it) },
                                            textStyle = arialTextStyle,
                                            colors = blackTextFieldColors,
                                            shape = RoundedCornerShape(10.dp),
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .testTag("profile_contact_phone_input"),
                                            singleLine = true,
                                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
                                        )
                                    }

                                    // Contact Relationship
                                    Column(
                                        modifier = Modifier.fillMaxWidth(),
                                        verticalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        Text(
                                            text = getLocalizedText("relationship", currentLang),
                                            fontFamily = ArialFontFamily,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 13.sp,
                                            color = DarkText
                                        )
                                        OutlinedTextField(
                                            value = profileContactRelation,
                                            onValueChange = { profileContactRelation = it },
                                            textStyle = arialTextStyle,
                                            colors = blackTextFieldColors,
                                            shape = RoundedCornerShape(10.dp),
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .testTag("profile_contact_relation_input"),
                                            singleLine = true
                                        )
                                    }
                                }
                            }
                        }

                        // System Settings / Preference Language
                        item {
                            Card(
                                colors = CardDefaults.cardColors(containerColor = Color.White),
                                shape = RoundedCornerShape(16.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(
                                    modifier = Modifier.padding(16.dp),
                                    verticalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(Icons.Default.Settings, contentDescription = null, tint = MutedText)
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = "Preferências",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 16.sp,
                                            color = DarkText
                                        )
                                    }
                                    Divider()
                                    
                                    // QR Code view mode toggle
                                    // Removed 'Apenas QR Code' switch as requested
                                    
                                    // Information display toggles
                                    Text("Informações na Ficha de Emergência", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color.Black, modifier = Modifier.padding(vertical = 8.dp))
                                    
                                    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                                        Text("Nome", fontSize = 13.sp, color = Color.Black)
                                        androidx.compose.material3.Switch(checked = showName, onCheckedChange = { showName = it })
                                    }
                                    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                                        Text("Contato de Emergência", fontSize = 13.sp, color = Color.Black)
                                        androidx.compose.material3.Switch(checked = showContact, onCheckedChange = { showContact = it })
                                    }
                                    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                                        Text("Tipo Sanguíneo", fontSize = 13.sp, color = Color.Black)
                                        androidx.compose.material3.Switch(checked = showBlood, onCheckedChange = { showBlood = it })
                                    }
                                    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                                        Text("Lista de Alergias", fontSize = 13.sp, color = Color.Black)
                                        androidx.compose.material3.Switch(checked = showAllergies, onCheckedChange = { showAllergies = it })
                                    }
                                    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                                        Text("QR Code", fontSize = 13.sp, color = Color.Black)
                                        androidx.compose.material3.Switch(checked = showQRCode, onCheckedChange = { showQRCode = it })
                                    }
                                    
                                    // Language selection dropdown
                                    val languages = listOf("Português (Brasil)", "English", "Español")
                                    var isLangDropdownOpen by remember { mutableStateOf(false) }

                                    Column(
                                        modifier = Modifier.fillMaxWidth(),
                                        verticalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        Text(
                                            text = getLocalizedText("language", currentLang),
                                            fontFamily = ArialFontFamily,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 13.sp,
                                            color = DarkText
                                        )
                                        Box(modifier = Modifier.fillMaxWidth()) {
                                            OutlinedTextField(
                                                value = profileLanguage,
                                                onValueChange = {},
                                                readOnly = true,
                                                textStyle = arialTextStyle,
                                                colors = blackTextFieldColors,
                                                trailingIcon = {
                                                    IconButton(onClick = { isLangDropdownOpen = true }) {
                                                        Icon(Icons.Default.ArrowDropDown, contentDescription = "Dropdown")
                                                    }
                                                },
                                                shape = RoundedCornerShape(10.dp),
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .clickable { isLangDropdownOpen = true }
                                                    .testTag("profile_lang_dropdown")
                                            )
                                            DropdownMenu(
                                                expanded = isLangDropdownOpen,
                                                onDismissRequest = { isLangDropdownOpen = false },
                                                modifier = Modifier
                                                    .fillMaxWidth(0.8f)
                                                    .background(Color.White)
                                            ) {
                                                languages.forEach { lang ->
                                                    DropdownMenuItem(
                                                        text = { Text(lang, fontFamily = ArialFontFamily, color = Color.Black) },
                                                        onClick = {
                                                            profileLanguage = lang
                                                            isLangDropdownOpen = false
                                                        },
                                                        modifier = Modifier.background(Color.White)
                                                    )
                                                }
                                            }
                                        }
                                    }

                                    // SOS Floating widget trigger toggle switch
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 4.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(
                                                text = getLocalizedText("emergency_toggle", currentLang),
                                                fontWeight = FontWeight.SemiBold,
                                                fontSize = 14.sp,
                                                color = DarkText
                                            )
                                            Text(
                                                text = "Exibe o botão de pânico SOS sobre a tela inicial.",
                                                fontSize = 11.sp,
                                                color = MutedText
                                            )
                                        }
                                        Switch(
                                            checked = profileIsAlertButtonEnabled,
                                            onCheckedChange = { profileIsAlertButtonEnabled = it },
                                            colors = SwitchDefaults.colors(checkedThumbColor = EmergencyRed, checkedTrackColor = EmergencyRedLight),
                                            modifier = Modifier.testTag("lockscreen_panic_toggle")
                                        )
                                    }
                                }
                            }
                        }

                        // Save Profile settings
                        item {
                            Spacer(modifier = Modifier.height(8.dp))
                            Button(
                                onClick = {
                                    val concatenatedAddress = listOf(
                                        profileAddressStreet,
                                        profileAddressNumber,
                                        profileAddressNeigh,
                                        profileAddressCity,
                                        profileAddressState
                                    ).joinToString(" | ")
                                    viewModel.saveProfile(
                                        name = profileName,
                                        phone = profilePhone,
                                        address = concatenatedAddress,
                                        bloodType = profileBloodType,
                                        emergencyContactName = profileContactName,
                                        emergencyContactPhone = profileContactPhone,
                                        emergencyContactRelation = profileContactRelation,
                                        isAlertButtonEnabled = profileIsAlertButtonEnabled,
                                        preferredLanguage = profileLanguage
                                    )
                                    Toast.makeText(context, getLocalizedText("profile_saved", currentLang), Toast.LENGTH_SHORT).show()
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = MedicalBlue),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier
                                    .width(200.dp)
                                    .height(50.dp)
                                    .testTag("save_profile_settings_button")
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Save,
                                        contentDescription = "Salvar Perfil",
                                        tint = Color.White,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = getLocalizedText("save", currentLang),
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 16.sp,
                                        color = Color.White
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(24.dp))
                        }
                    }
                }

                // Permanent lockscreen simulated alert floating panic button (Only visible when active in Profile settings)
                if (profileIsAlertButtonEnabled) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(bottom = 90.dp, end = 20.dp)
                    ) {
                        // Pulsing backdrop shadow 1 (Concentric outer ring)
                        Box(
                            modifier = Modifier
                                .size(76.dp)
                                .scale(pulseScale1)
                                .clip(CircleShape)
                                .background(EmergencyRed.copy(alpha = pulseAlpha1))
                        )
                        // Pulsing backdrop shadow 2 (Concentric inner ring)
                        Box(
                            modifier = Modifier
                                .size(76.dp)
                                .scale(pulseScale2)
                                .clip(CircleShape)
                                .background(EmergencyRed.copy(alpha = pulseAlpha2))
                        )
                        // QR Code trigger
                        IconButton(onClick = { 
                            isQRCodeOpen = true 
                        }) {
                            Icon(Icons.Default.QrCode, contentDescription = "QR Code", tint = MedicalBlue)
                        }
                        // The core floating panic button
                        Box(
                            modifier = Modifier
                                .size(76.dp)
                                .clip(CircleShape)
                                .background(EmergencyRed)
                                .clickable { isEmergencyCardOpen = true }
                                .border(width = 3.dp, color = Color.White, shape = CircleShape)
                                .testTag("floating_sos_panic_button"),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Warning,
                                    contentDescription = "Emergency Medical SOS Widget",
                                    tint = Color.White,
                                    modifier = Modifier.size(30.dp)
                                )
                                Text(
                                    text = "SOS",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Black,
                                    color = Color.White
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    // --- QR CODE DIALOG MODAL ---
    if (isQRCodeOpen) {
        Dialog(onDismissRequest = { isQRCodeOpen = false }) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .widthIn(max = 400.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("QR Code de Emergência", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    val healthDataString = """
                        NOME: ${dbProfile?.name ?: ""}
                        SANGUE: ${dbProfile?.bloodType ?: ""}
                        ALERGIAS: ${dbAllergies.joinToString { it.allergen }}
                        CONTATOS: ${dbContacts.joinToString { it.name }}
                    """.trimIndent()
                    
                    val encryptedData = encryptEmergencyData(healthDataString, "0000")
                    val qrUrl = "https://api.qrserver.com/v1/create-qr-code/?size=250x250&data=https://soualergico.vercel.app/ficha/#$encryptedData"
                    
                    Image(
                        painter = coil.compose.rememberAsyncImagePainter(qrUrl),
                        contentDescription = "QR Code",
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Escaneie para visualizar seus dados.", textAlign = TextAlign.Center)
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = { isQRCodeOpen = false }) { Text("Fechar") }
                }
            }
        }
    }

    // 0. CONFIRM DELETE DIALOG MODAL
    allergyToDelete?.let { allergy ->
        Dialog(onDismissRequest = { allergyToDelete = null }) {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFFEE2E2)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = null,
                            tint = Color(0xFFEF4444),
                            modifier = Modifier.size(28.dp)
                        )
                    }

                    Text(
                        text = getLocalizedText("confirm_delete_title", currentLang),
                        fontFamily = ArialFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = Color.Black,
                        textAlign = TextAlign.Center
                    )

                    Text(
                        text = "${getLocalizedText("confirm_delete_message", currentLang)}\n\"${allergy.allergen}\"",
                        fontFamily = ArialFontFamily,
                        fontSize = 14.sp,
                        color = Color.Gray,
                        textAlign = TextAlign.Center,
                        lineHeight = 20.sp
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Cancel button
                        OutlinedButton(
                            onClick = { allergyToDelete = null },
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Gray),
                            border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = getLocalizedText("confirm_delete_no", currentLang),
                                fontFamily = ArialFontFamily,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        // Confirm delete button
                        Button(
                            onClick = {
                                viewModel.removeAllergy(allergy)
                                allergyToDelete = null
                                Toast.makeText(context, "Alergia excluída com sucesso!", Toast.LENGTH_SHORT).show()
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEF4444)),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = getLocalizedText("confirm_delete_yes", currentLang),
                                fontFamily = ArialFontFamily,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }
    }

    // 1. ALLERGY DETAIL DIALOG MODAL
    activeAllergyDetail?.let { allergy ->
        Dialog(
            onDismissRequest = { activeAllergyDetail = null },
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier
                    .fillMaxWidth(0.92f)
                    .widthIn(max = 500.dp)
                    .padding(16.dp)
                    .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(24.dp))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Header Category Details
                    val catColor = when (allergy.severity) {
                        "Medicamento" -> Color(0xFFF43F5E)
                        "Alimentar" -> Color(0xFFF59E0B)
                        else -> Color(0xFF0EA5E9)
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(catColor.copy(alpha = 0.12f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = when (allergy.severity) {
                                    "Medicamento" -> Icons.Default.MedicalServices
                                    "Alimentar" -> Icons.Default.Restaurant
                                    else -> Icons.Default.Warning
                                },
                                contentDescription = null,
                                tint = catColor,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = allergy.severity.uppercase(),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = catColor,
                                letterSpacing = 1.sp
                            )
                            Text(
                                text = "Dados da Alergia",
                                fontSize = 14.sp,
                                color = MutedText
                            )
                        }
                        IconButton(onClick = { activeAllergyDetail = null }) {
                            Icon(Icons.Default.Close, contentDescription = "Fechar", tint = MutedText)
                        }
                    }

                    Divider()

                    // Allergen Name
                    Column {
                        Text(
                            text = "Alergênio",
                            fontSize = 12.sp,
                            color = MutedText,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Sou alérgico a ${allergy.allergen}",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = DarkText
                        )
                    }

                    // Symptoms
                    Column {
                        Text(
                            text = "Sintomas Manifestados",
                            fontSize = 12.sp,
                            color = MutedText,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = allergy.symptoms,
                            fontSize = 15.sp,
                            color = DarkText,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    // Prescription Guidance
                    if (allergy.rescueMedication.isNotBlank()) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(SoftGrey)
                                .padding(12.dp)
                        ) {
                            Text(
                                text = "Instruções do Médico",
                                fontSize = 11.sp,
                                color = MedicalBlue,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.5.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = allergy.rescueMedication,
                                fontSize = 14.sp,
                                color = DarkText,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }

                    // View Attachment Placement
                    if (!allergy.prescriptionFilePath.isNullOrBlank()) {
                        val file = File(allergy.prescriptionFilePath)
                        if (file.exists()) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(MedicalBlueLight)
                                    .clickable {
                                        try {
                                            val uri = FileProvider.getUriForFile(
                                                context,
                                                "${context.packageName}.provider",
                                                file
                                            )
                                            val intent = Intent(Intent.ACTION_VIEW)
                                            intent.setDataAndType(
                                                uri,
                                                if (file.extension == "pdf") "application/pdf" else "image/*"
                                            )
                                            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                                            context.startActivity(intent)
                                        } catch (e: Exception) {
                                            Toast.makeText(context, "Erro ao abrir arquivo", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                    .padding(14.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.PictureAsPdf, contentDescription = null, tint = MedicalBlue)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "Visualizar Receita Médica Anexada",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MedicalBlue
                                    )
                                }
                            }
                        }
                    }

                    // Close Button
                    Button(
                        onClick = { activeAllergyDetail = null },
                        colors = ButtonDefaults.buttonColors(containerColor = MedicalBlue),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                    ) {
                        Text(getLocalizedText("close", currentLang), fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }

    // 2. FULLSCREEN EMERGENCY CARD MODAL (QR CODE GENERATOR)
    if (isEmergencyCardOpen) {
        Dialog(
            onDismissRequest = { isEmergencyCardOpen = false },
            properties = DialogProperties(
                usePlatformDefaultWidth = false,
                dismissOnBackPress = true,
                dismissOnClickOutside = false
            )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(EmergencyRed)
                    .systemBarsPadding()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    // Header Area
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(top = 10.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = "SOS",
                            tint = Color.White,
                            modifier = Modifier.size(44.dp)
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = getLocalizedText("emergency_card", currentLang).uppercase(),
                            fontWeight = FontWeight.Black,
                            fontSize = 20.sp,
                            color = Color.White,
                            textAlign = TextAlign.Center
                        )
                        // Toggle for view mode
                        // Removed 'Apenas QR Code' switch as requested
                    }

                    // Scannable QR Code card section
                    if (showQRCode) {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            shape = RoundedCornerShape(24.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                            modifier = Modifier
                                .size(240.dp)
                                .padding(12.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(12.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                // Construct payload containing user information and allergy profile
                                val qrUrl = "https://api.qrserver.com/v1/create-qr-code/?size=250x250&data=https://soualergico.vercel.app/emergency/user-id-123"
                                
                                Image(
                                    painter = coil.compose.rememberAsyncImagePainter(qrUrl),
                                    contentDescription = "QR Code",
                                    modifier = Modifier.fillMaxSize()
                                )
                            }
                        }
                    }

                    // Readable Data for First Responders Scroll
                    if (showName || showContact || showBlood || showAllergies) {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                                .padding(vertical = 12.dp)
                        ) {
                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                // User Info
                                if (showName) {
                                    item {
                                        Column {
                                            Text("Nome Completo", fontSize = 11.sp, color = MutedText, fontWeight = FontWeight.Bold)
                                            Text(
                                                text = profileName.ifBlank { "Não Cadastrado" },
                                                fontSize = 15.sp,
                                                fontWeight = FontWeight.ExtraBold,
                                                color = DarkText
                                            )
                                        }
                                    }
                                }

                                // Blood Type
                                if (showBlood) {
                                    item {
                                        Column {
                                            Text("Tipo Sanguíneo", fontSize = 11.sp, color = MutedText, fontWeight = FontWeight.Bold)
                                            Box(
                                                modifier = Modifier
                                                    .padding(top = 2.dp)
                                                    .clip(RoundedCornerShape(6.dp))
                                                    .background(EmergencyRedLight)
                                                    .padding(horizontal = 8.dp, vertical = 3.dp)
                                            ) {
                                                Text(
                                                    text = profileBloodType,
                                                    fontSize = 14.sp,
                                                    fontWeight = FontWeight.Black,
                                                    color = EmergencyRed
                                                )
                                            }
                                        }
                                    }
                                }

                                // Emergency Contacts
                                if (showContact) {
                                    item {
                                        Column {
                                            Text("Contato de Emergência", fontSize = 11.sp, color = MutedText, fontWeight = FontWeight.Bold)
                                            if (profileContactName.isNotBlank() || profileContactPhone.isNotBlank()) {
                                                Text(
                                                    text = "$profileContactName ($profileContactRelation)",
                                                    fontSize = 14.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = DarkText
                                                )
                                                Text(
                                                    text = profileContactPhone,
                                                    fontSize = 14.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = MedicalBlue
                                                )
                                            } else {
                                                Text("Nenhum contato cadastrado", fontSize = 13.sp, color = MutedText)
                                            }
                                        }
                                    }
                                }

                                // Allergies and symptoms list
                                if (showAllergies) {
                                    item {
                                        Divider()
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text("Alergias Cadastradas", fontSize = 11.sp, color = MutedText, fontWeight = FontWeight.Bold)
                                    }

                                    if (dbAllergies.isEmpty()) {
                                        item {
                                            Text("Nenhuma alergia adicionada", fontSize = 13.sp, color = MutedText)
                                        }
                                    } else {
                                        items(dbAllergies) { allergy ->
                                            Column(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .border(1.dp, Color(0xFFF1F5F9), RoundedCornerShape(8.dp))
                                                    .padding(8.dp)
                                            ) {
                                                Row(verticalAlignment = Alignment.CenterVertically) {
                                                    Box(
                                                        modifier = Modifier
                                                            .size(8.dp)
                                                            .clip(CircleShape)
                                                            .background(EmergencyRed)
                                                    )
                                                    Spacer(modifier = Modifier.width(6.dp))
                                                    Text(
                                                        text = allergy.allergen,
                                                        fontWeight = FontWeight.Black,
                                                        fontSize = 14.sp,
                                                        color = DarkText
                                                    )
                                                }
                                                Text(
                                                    text = "Sintomas: ${allergy.symptoms}",
                                                    fontSize = 12.sp,
                                                    color = MutedText
                                                )
                                                if (allergy.rescueMedication.isNotBlank()) {
                                                    Text(
                                                        text = "Prescrição: ${allergy.rescueMedication}",
                                                        fontSize = 12.sp,
                                                        color = DarkText,
                                                        fontWeight = FontWeight.SemiBold
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // Exit Close Card button
                    Button(
                        onClick = { isEmergencyCardOpen = false },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp)
                    ) {
                        Text(
                            text = getLocalizedText("close", currentLang).uppercase(),
                            color = EmergencyRed,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Black
                        )
                    }
                }
            }
        }
    }
}
