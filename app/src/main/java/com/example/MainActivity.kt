package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.example.data.AppDatabase
import com.example.data.AppRepository
import com.example.ui.SafeMyAlertsApp
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.AllergyViewModel
import com.example.ui.viewmodel.AllergyViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Show over lock screen
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true)
            setTurnScreenOn(true)
        } else {
            @Suppress("DEPRECATION")
            window.addFlags(
                android.view.WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or
                android.view.WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
            )
        }
        
        enableEdgeToEdge()

        val database = AppDatabase.getDatabase(applicationContext)
        val repository = AppRepository(database.appDao())
        val factory = AllergyViewModelFactory(repository)
        val viewModel: AllergyViewModel by viewModels { factory }

        setContent {
            MyApplicationTheme {
                SafeMyAlertsApp(viewModel)
            }
        }
    }
}
