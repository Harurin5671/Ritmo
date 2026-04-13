package com.app.crowns.ritmo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.app.crowns.ritmo.core.datastore.UserPreferencesDataStore
import com.app.crowns.ritmo.core.navigation.AppNavHost
import com.app.crowns.ritmo.ui.theme.RitmoTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RitmoTheme {

                AppNavHost()
            }
        }
    }
}