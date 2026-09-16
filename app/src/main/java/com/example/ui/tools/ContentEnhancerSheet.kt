package com.example.ui.tools

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*
import com.example.network.*
import com.example.BuildConfig
import kotlinx.coroutines.launch
import kotlinx.coroutines.Dispatchers

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContentEnhancerSheet(
  initialText: String,
  onApply: (String) -> Unit,
  onDismiss: () -> Unit
) {
  var rawText by remember { mutableStateOf(initialText) }
  var jobDescription by remember { mutableStateOf("") }
  var selectedTone by remember { mutableStateOf("Professional") }
  val tones = listOf("Professional", "Executive", "Concise", "Technical", "Entry-Level")
  
  var suggestions by remember { mutableStateOf<List<BulletSuggestion>>(emptyList()) }
  var isLoading by remember { mutableStateOf(false) }
  var errorMessage by remember { mutableStateOf<String?>(null) }
  val coroutineScope = rememberCoroutineScope()

  // Generate local baseline suggestions initially
  LaunchedEffect(rawText, selectedTone) {
      if (!isLoading) {
         suggestions = generateSuggestions(rawText, selectedTone)
      }
  }

  ModalBottomSheet(
    onDismissRequest = onDismiss,
    containerColor = WarmIvory
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 24.dp)
        .padding(bottom = 36.dp)
        .verticalScroll(rememberScrollState())
    ) {
      Text("Achievement & Content Assistant", style = MaterialTheme.typography.titleLarge, color = NearBlack)
      Text(
        "Convert passive duty descriptions into high-impact, outcome-oriented bullets.",
        style = MaterialTheme.typography.bodySmall,
        color = TextSecondary,
        modifier = Modifier.padding(top = 2.dp, bottom = 14.dp)
      )

      OutlinedTextField(
        value = rawText,
        onValueChange = { rawText = it },
        modifier = Modifier
          .fillMaxWidth()
          .height(100.dp),
        label = { Text("Original bullet or responsibility") },
        colors = OutlinedTextFieldDefaults.colors(
          focusedContainerColor = Color.White,
          unfocusedContainerColor = Color.White,
          focusedBorderColor = BurgundyPrimary,
          unfocusedBorderColor = WarmStoneBorder
        ),
        shape = RoundedCornerShape(8.dp)
      )
      
      Spacer(modifier = Modifier.height(12.dp))
      
      OutlinedTextField(
        value = jobDescription,
        onValueChange = { jobDescription = it },
        modifier = Modifier
          .fillMaxWidth()
          .height(80.dp),
        label = { Text("Target Job Description (Optional)") },
        placeholder = { Text("Paste JD snippets here to align keywords", color = TextSecondary, fontSize = 12.sp) },
        colors = OutlinedTextFieldDefaults.colors(
          focusedContainerColor = Color.White,
          unfocusedContainerColor = Color.White,
          focusedBorderColor = BurgundyPrimary,
          unfocusedBorderColor = WarmStoneBorder
        ),
        shape = RoundedCornerShape(8.dp)
      )

      Spacer(modifier = Modifier.height(12.dp))

      // Tone Selectors
      Text("Target Style & Tone", style = MaterialTheme.typography.labelMedium, color = NearBlack)
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(top = 6.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        tones.forEach { tone ->
          FilterChip(
            selected = selectedTone == tone,
            onClick = { selectedTone = tone },
            label = { Text(tone, fontSize = 11.sp) },
            colors = FilterChipDefaults.filterChipColors(
              selectedContainerColor = BurgundyPrimary,
              selectedLabelColor = WarmIvory
            )
          )
        }
      }

      Spacer(modifier = Modifier.height(12.dp))

      Button(
        onClick = {
            if (rawText.isNotBlank()) {
                isLoading = true
                errorMessage = null
                coroutineScope.launch(Dispatchers.IO) {
                    try {
                        val apiKey = BuildConfig.GEMINI_API_KEY
                        if (apiKey.isBlank() || apiKey == "MY_GEMINI_API_KEY") {
                            launch(Dispatchers.Main) {
                                errorMessage = "Invalid API Key. Using local suggestions."
                                suggestions = generateSuggestions(rawText, selectedTone)
                                isLoading = false
                            }
                            return@launch
                        }

                        val prompt = buildString {
                            append("Rewrite the following resume bullet point to be high-impact, outcome-oriented, and in a '$selectedTone' tone. ")
                            if (jobDescription.isNotBlank()) {
                                append("Crucially, try to align the wording and emphasize skills relevant to this Job Description snippet: '$jobDescription'. ")
                            }
                            append("\n\nOriginal text: '$rawText'\n")
                            append("\nProvide exactly two distinct variations of the bullet point. Format the response as: \nHeadline 1\nBullet 1\nHeadline 2\nBullet 2")
                        }

                        val request = GenerateContentRequest(
                            contents = listOf(Content(parts = listOf(Part(text = prompt)))),
                            generationConfig = GenerationConfig(temperature = 0.7f),
                            systemInstruction = Content(parts = listOf(Part(text = "You are an expert executive resume writer.")))
                        )
                        
                        val response = RetrofitClient.service.generateContent(apiKey, request)
                        val text = response.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text ?: ""
                        
                        val newSuggestions = mutableListOf<BulletSuggestion>()
                        val lines = text.lines().filter { it.isNotBlank() }
                        if (lines.size >= 4) {
                            newSuggestions.add(BulletSuggestion(lines[0].removePrefix("**").removeSuffix("**"), lines[1].removePrefix("- ").removePrefix("* ")))
                            newSuggestions.add(BulletSuggestion(lines[2].removePrefix("**").removeSuffix("**"), lines[3].removePrefix("- ").removePrefix("* ")))
                        } else {
                             newSuggestions.add(BulletSuggestion("AI Suggestion", text))
                        }

                        launch(Dispatchers.Main) {
                            suggestions = newSuggestions
                            isLoading = false
                        }
                    } catch (e: Exception) {
                        launch(Dispatchers.Main) {
                            errorMessage = "Failed to connect to AI: ${e.message}"
                            isLoading = false
                            suggestions = generateSuggestions(rawText, selectedTone)
                        }
                    }
                }
            }
        },
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(containerColor = BurgundyPrimary),
        shape = RoundedCornerShape(8.dp),
        enabled = !isLoading
      ) {
        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.size(20.dp), color = WarmIvory, strokeWidth = 2.dp)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Enhancing with AI...", color = WarmIvory)
        } else {
            Icon(Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text("Enhance with AI", color = WarmIvory)
        }
      }

      if (errorMessage != null) {
          Spacer(modifier = Modifier.height(8.dp))
          Text(errorMessage!!, color = MaterialTheme.colorScheme.error, fontSize = 12.sp)
      }

      Spacer(modifier = Modifier.height(16.dp))
      Text("Polished Achievement Formulations", style = MaterialTheme.typography.titleSmall, color = BurgundyPrimary)
      Spacer(modifier = Modifier.height(8.dp))
      
      suggestions.forEach { suggestion ->
        Card(
          modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
          colors = CardDefaults.cardColors(containerColor = Color.White),
          border = BorderStroke(1.dp, WarmStoneBorder),
          shape = RoundedCornerShape(8.dp)
        ) {
          Column(modifier = Modifier.padding(14.dp)) {
            Text(
              suggestion.headline,
              style = MaterialTheme.typography.labelSmall,
              color = BurgundyPrimary,
              fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
              suggestion.bullet,
              style = MaterialTheme.typography.bodyMedium,
              color = NearBlack
            )
            Spacer(modifier = Modifier.height(10.dp))
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.End
            ) {
              OutlinedButton(
                onClick = {
                  onApply(suggestion.bullet)
                  onDismiss()
                },
                colors = ButtonDefaults.outlinedButtonColors(contentColor = BurgundyPrimary),
                border = BorderStroke(1.dp, BurgundyPrimary),
                shape = RoundedCornerShape(6.dp)
              ) {
                Text("Use This Bullet", fontSize = 11.5.sp)
              }
            }
          }
        }
      }
    }
  }
}

data class BulletSuggestion(
  val headline: String,
  val bullet: String
)

private fun generateSuggestions(input: String, tone: String): List<BulletSuggestion> {
  val clean = input.trim().removePrefix("•").trim()
  if (clean.isBlank()) {
    return listOf(
      BulletSuggestion(
        "Action + Scope + Metric",
        "Spearheaded strategic initiative across 4 core workflows, improving operational throughput by [X]%."
      ),
      BulletSuggestion(
        "Process Optimization",
        "Streamlined cross-department communication protocols, reducing turnaround time from [X] days to [Y] hours."
      )
    )
  }
  return when (tone) {
    "Executive" -> listOf(
      BulletSuggestion(
        "Strategic Leadership",
        "Orchestrated cross-functional delivery of $clean, aligning multi-stakeholder governance and delivering [X]% efficiency gains."
      ),
      BulletSuggestion(
        "P&L & Performance",
        "Directed full lifecycle execution for $clean, driving measurable performance optimization and risk reduction."
      )
    )
    "Concise" -> listOf(
      BulletSuggestion(
        "High Impact / Low Word Count",
        "Delivered $clean, accelerating team velocity and reducing blocker cycle time."
      ),
      BulletSuggestion(
        "Direct Action",
        "Engineered and deployed $clean to enhance product reliability and user satisfaction."
      )
    )
    "Technical" -> listOf(
      BulletSuggestion(
        "Engineering Precision",
        "Architected scalable solution for $clean, maintaining 99.9% uptime SLA and automated test coverage."
      ),
      BulletSuggestion(
        "Systems Refactoring",
        "Refactored mission-critical pipeline for $clean, reducing processing latency and resource utilization by [X]%."
      )
    )
    "Entry-Level" -> listOf(
      BulletSuggestion(
        "Proactive Contribution",
        "Collaborated with cross-functional mentors to implement $clean, completing deliverables ahead of project schedule."
      ),
      BulletSuggestion(
        "Analytical Focus",
        "Conducted thorough research and execution for $clean, contributing to a [X]% enhancement in team outputs."
      )
    )
    else -> listOf(
      BulletSuggestion(
        "Achievement-Oriented",
        "Successfully managed $clean, resulting in improved quality metrics and [X]% increased productivity."
      ),
      BulletSuggestion(
        "Quantified Impact",
        "Executed $clean across key business units, achieving key target milestones within budget and schedule."
      )
    )
  }
}
