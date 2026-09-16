package com.example.ui.editor

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.ResumeSettings
import com.example.ui.theme.*

@Composable
fun ThemeCustomizationCard(
    settings: ResumeSettings,
    onChange: (ResumeSettings) -> Unit
) {
    val colors = listOf(
        "#72232B", // Burgundy
        "#1F1F1F", // Near Black
        "#2E5B4B", // Forest Green
        "#2B4561", // Navy Blue
        "#8B5A2B", // Brown
        "#4A4A4A"  // Grey
    )

    val fonts = listOf(
        "Inter / Swiss Sans",
        "Playfair Display / Serif",
        "Fira Code / Mono"
    )

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, WarmStoneBorder),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Palette, contentDescription = null, tint = BurgundyPrimary, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Theme Customization", color = NearBlack, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            }
            
            Spacer(modifier = Modifier.height(16.dp))

            Text("Accent Color", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = NearBlack)
            Spacer(modifier = Modifier.height(8.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(colors) { hex ->
                    val color = try { Color(android.graphics.Color.parseColor(hex)) } catch (e: Exception) { Color.Black }
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(color)
                            .border(
                                width = if (settings.primaryColorHex == hex) 2.dp else 1.dp,
                                color = if (settings.primaryColorHex == hex) BurgundyPrimary else WarmStoneBorder,
                                shape = CircleShape
                            )
                            .clickable { onChange(settings.copy(primaryColorHex = hex)) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text("Typography Style", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = NearBlack)
            Spacer(modifier = Modifier.height(8.dp))
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                fonts.forEach { fontChoice ->
                    val fontFamily = when(fontChoice) {
                        "Playfair Display / Serif" -> FontFamily.Serif
                        "Fira Code / Mono" -> FontFamily.Monospace
                        else -> FontFamily.SansSerif
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(6.dp))
                            .background(if (settings.fontChoice == fontChoice) BurgundyPrimary.copy(alpha = 0.1f) else Color.Transparent)
                            .border(1.dp, if (settings.fontChoice == fontChoice) BurgundyPrimary else WarmStoneBorder, RoundedCornerShape(6.dp))
                            .clickable { onChange(settings.copy(fontChoice = fontChoice)) }
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = fontChoice,
                            fontFamily = fontFamily,
                            fontSize = 13.sp,
                            color = NearBlack
                        )
                    }
                }
            }
        }
    }
}
