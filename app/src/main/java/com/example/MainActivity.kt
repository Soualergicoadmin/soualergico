package com.example

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FormatListBulleted
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.ui.screens.*
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.Primary
import com.example.ui.theme.PrimaryContainer
import com.example.ui.theme.Secondary
import com.example.viewmodel.MainViewModel
import androidx.appcompat.app.AppCompatActivity
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode

class ScanActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan)

        // Exemplo: Disparar ao clicar no botão de escanear
        iniciarLeituraQRCode()
    }

    private fun iniciarLeituraQRCode() {
        // Configura o leitor para aceitar apenas QR Code
        val options = GmsBarcodeScannerOptions.Builder()
            .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
            .enableAutoZoom() // Zoom automático se o QR Code estiver longe
            .build()

        val scanner = GmsBarcodeScanning.getClient(this, options)

        scanner.startScan()
            .addOnSuccessListener { barcode ->
                // QR Code lido com sucesso!
                val conteudoLido = barcode.rawValue
                
                if (conteudoLido != null) {
                    // Exemplo: Se for uma URL da ficha, você pode abrir no navegador ou buscar na API
                    Toast.makeText(this, "Lido: $conteudoLido", Toast.LENGTH_LONG).show()
                    
                    // Aqui você processa a URL ou o ID lido...
                    processarConteudoLido(conteudoLido)
                }
            }
            .addOnCanceledListener {
                // Usuário fechou a câmera sem escanear
                Toast.makeText(this, "Leitura cancelada", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                // Erro ao abrir a câmera/scanner
                Toast.makeText(this, "Erro ao ler QR Code: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun processarConteudoLido(urlOuTexto: String) {
        // Lógica para direcionar o usuário ou abrir os dados
    }
}

class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                MainAppContainer(viewModel)
            }
        }
    }
}

@Composable
fun MainAppContainer(viewModel: MainViewModel) {
    val navController = rememberNavController()
    val isUserLoggedIn by viewModel.isUserLoggedIn.collectAsState()
    val toastMessage by viewModel.toastMessage.collectAsState()
    val context = LocalContext.current

    // Observe and display any state toasts beautifully in Compose
    LaunchedEffect(toastMessage) {
        toastMessage?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            viewModel.clearToast()
        }
    }

    // Automatically navigate when login state changes
    LaunchedEffect(isUserLoggedIn) {
        if (isUserLoggedIn) {
            navController.navigate("home") {
                popUpTo("login") { inclusive = true }
            }
        } else {
            navController.navigate("splash") {
                popUpTo(0) { inclusive = true }
            }
        }
    }

    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route

    // Define bottom nav bar destinations
    val bottomNavRoutes = listOf("home", "medication_list", "allergy_list", "profile")

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            if (isUserLoggedIn && currentRoute in bottomNavRoutes) {
                BottomNavBar(
                    currentRoute = currentRoute ?: "home",
                    onNavigate = { route ->
                        navController.navigate(route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "splash",
            modifier = Modifier.padding(innerPadding)
        ) {
            // 1. Splash Screen
            composable("splash") {
                SplashScreen(onNavigateToNext = {
                    viewModel.login() // Auto logs in to provide standard fast prototype experience
                })
            }

            // 2. Login Screen
            composable("login") {
                LoginScreen(onLoginSuccess = {
                    viewModel.login()
                })
            }

            // 3. Home Screen
            composable("home") {
                HomeScreen(
                    viewModel = viewModel,
                    onNavigateToAddAllergy = { navController.navigate("add_allergy") },
                    onNavigateToAddMedication = { navController.navigate("add_medication") },
                    onNavigateToAllergyList = { navController.navigate("allergy_list") },
                    onNavigateToMedicationList = { navController.navigate("medication_list") }
                )
            }

            // 4. Medication List Screen
            composable("medication_list") {
                MedicationListScreen(
                    viewModel = viewModel,
                    onNavigateToAddMedication = { navController.navigate("add_medication") }
                )
            }

            // 5. Allergy List Screen
            composable("allergy_list") {
                AllergyListScreen(
                    viewModel = viewModel,
                    onNavigateToAddAllergy = { navController.navigate("add_allergy") },
                    onClose = { navController.popBackStack() }
                )
            }

            // 6. Add Allergy Screen
            composable("add_allergy") {
                AddAllergyScreen(
                    viewModel = viewModel,
                    onBack = { navController.popBackStack() }
                )
            }

            // 7. Add Medication Screen
            composable("add_medication") {
                AddMedicationScreen(
                    viewModel = viewModel,
                    onBack = { navController.popBackStack() }
                )
            }

            // 8. Profile Screen
            composable("profile") {
                ProfileScreen(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun BottomNavBar(
    currentRoute: String,
    onNavigate: (String) -> Unit
) {
    val items = listOf(
        NavigationItem("home", "Início", Icons.Default.Home),
        NavigationItem("medication_list", "Medicação", Icons.Default.MedicalServices),
        NavigationItem("allergy_list", "Alergias", Icons.Default.FormatListBulleted),
        NavigationItem("profile", "Perfil", Icons.Default.Person)
    )

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)),
        color = Color(0xFFEBEFEE),
        tonalElevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .navigationBarsPadding(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            items.forEach { item ->
                val isSelected = currentRoute == item.route
                val contentColor = if (isSelected) Color.White else Secondary
                val containerColor = if (isSelected) PrimaryContainer else Color.Transparent

                Column(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(containerColor)
                        .clickable { onNavigate(item.route) }
                        .padding(horizontal = 16.dp, vertical = 6.dp)
                        .testTag("nav_btn_${item.route}"),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.label,
                        tint = contentColor,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = item.label,
                        color = contentColor,
                        fontSize = 11.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                    )
                }
            }
        }
    }
}

data class NavigationItem(
    val route: String,
    val label: String,
    val icon: ImageVector
)
