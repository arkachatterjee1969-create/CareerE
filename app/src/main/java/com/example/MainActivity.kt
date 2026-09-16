package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.brand.BrandIdentityScreen
import com.example.ui.studio.CareereStudioScreen
import com.example.ui.theme.CareereTheme

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      CareereTheme {
        var currentScreen by remember { mutableStateOf("brand") }

        Crossfade(targetState = currentScreen, label = "ScreenTransition") { screen ->
          when (screen) {
            "studio" -> {
              CareereStudioScreen(
                onNavigateToBrandIdentity = { currentScreen = "brand" }
              )
            }
            "brand" -> {
              BrandIdentityScreen(
                onNavigateToStudio = { currentScreen = "studio" }
              )
            }
          }
        }
      }
    }
  }
}

