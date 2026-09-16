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
import com.example.model.ResumeData
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun JobMatcherSheet(
  resume: ResumeData,
  onDismiss: () -> Unit
) {
  var jobText by remember { mutableStateOf("") }
  var hasAnalyzed by remember { mutableStateOf(false) }

  val existingKeywords = remember(resume) {
    val set = mutableSetOf<String>()
    resume.skills.forEach { set.add(it.name.lowercase().trim()) }
    resume.experiences.forEach { exp ->
      set.addAll(exp.bullets.flatMap { it.lowercase().split(Regex("\\W+")) }.filter { it.length > 3 })
    }
    set
  }

  // Keywords extraction logic
  val analysisResults = remember(hasAnalyzed, jobText) {
    if (!hasAnalyzed || jobText.isBlank()) null
    else {
      val jdWords = jobText.lowercase()
        .split(Regex("[\\s,;.:()\\[\\]/\"]+"))
        .filter { it.length > 3 && !isCommonStopword(it) }
        .groupingBy { it }
        .eachCount()
        .toList()
        .sortedByDescending { it.second }
        .take(20)

      val matched = jdWords.filter { (word, _) -> existingKeywords.any { it.contains(word) } }
      val missing = jdWords.filter { (word, _) -> !existingKeywords.any { it.contains(word) } }

      val matchScore = if (jdWords.isNotEmpty()) {
        ((matched.size.toFloat() / jdWords.size) * 100).toInt()
      } else 0

      Triple(matchScore, matched, missing)
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
      Text("Job Description Matcher", style = MaterialTheme.typography.titleLarge, color = NearBlack)
      Text(
        "Compare your CV against target job requirements. Guidance only — never fabricate qualifications.",
        style = MaterialTheme.typography.bodySmall,
        color = TextSecondary,
        modifier = Modifier.padding(top = 2.dp, bottom = 14.dp)
      )

      OutlinedTextField(
        value = jobText,
        onValueChange = { jobText = it },
        modifier = Modifier
          .fillMaxWidth()
          .height(130.dp),
        placeholder = { Text("Paste job posting description, key responsibilities, or requirements here...", fontSize = 12.sp) },
        colors = OutlinedTextFieldDefaults.colors(
          focusedContainerColor = Color.White,
          unfocusedContainerColor = Color.White,
          focusedBorderColor = BurgundyPrimary,
          unfocusedBorderColor = WarmStoneBorder
        ),
        shape = RoundedCornerShape(8.dp)
      )

      Spacer(modifier = Modifier.height(12.dp))

      Button(
        onClick = { hasAnalyzed = true },
        enabled = jobText.isNotBlank(),
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(containerColor = BurgundyPrimary)
      ) {
        Icon(Icons.Default.ManageSearch, contentDescription = null, modifier = Modifier.size(18.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Text("Analyze Match & Missing Keywords", color = WarmIvory)
      }

      analysisResults?.let { (score, matched, missing) ->
        Spacer(modifier = Modifier.height(20.dp))

        // Match Score Card
        Card(
          modifier = Modifier.fillMaxWidth(),
          colors = CardDefaults.cardColors(containerColor = Color.White),
          border = BorderStroke(1.dp, WarmStoneBorder),
          shape = RoundedCornerShape(10.dp)
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column {
              Text("Keyword Alignment", style = MaterialTheme.typography.titleMedium, color = NearBlack)
              Text("Based on top recurring terms in the JD", style = MaterialTheme.typography.bodySmall, color = TextSecondary)
            }
            Surface(
              color = if (score >= 60) StatusSuccess.copy(alpha = 0.15f) else StatusWarning.copy(alpha = 0.15f),
              shape = RoundedCornerShape(8.dp)
            ) {
              Text(
                "$score%",
                color = if (score >= 60) StatusSuccess else StatusWarning,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Matched Keywords
        Text("Matching Competencies (${matched.size})", style = MaterialTheme.typography.titleSmall, color = StatusSuccess)
        FlowRow(
          modifier = Modifier
            .fillMaxWidth()
            .padding(top = 6.dp),
          horizontalArrangement = Arrangement.spacedBy(6.dp),
          verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          matched.forEach { (word, count) ->
            Surface(
              color = StatusSuccess.copy(alpha = 0.1f),
              shape = RoundedCornerShape(6.dp),
              border = BorderStroke(0.8.dp, StatusSuccess.copy(alpha = 0.3f))
            ) {
              Text(
                "✓ $word ($count)",
                fontSize = 11.sp,
                color = StatusSuccess,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Missing Keywords
        Text("Missing Key Terms (${missing.size})", style = MaterialTheme.typography.titleSmall, color = BurgundyPrimary)
        Text(
          "If you possess these competencies, consider highlighting them in your experience bullets or skills list:",
          style = MaterialTheme.typography.bodySmall,
          color = TextSecondary,
          modifier = Modifier.padding(top = 2.dp, bottom = 6.dp)
        )
        FlowRow(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(6.dp),
          verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          missing.forEach { (word, count) ->
            Surface(
              color = WarmStoneLight,
              shape = RoundedCornerShape(6.dp),
              border = BorderStroke(0.8.dp, WarmStoneBorder)
            ) {
              Text(
                "+ $word",
                fontSize = 11.sp,
                color = NearBlack,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
              )
            }
          }
        }
      }
    }
  }
}

private fun isCommonStopword(word: String): Boolean {
  val stopwords = setOf(
    "that", "this", "with", "from", "have", "will", "your", "their", "about",
    "must", "should", "ability", "working", "work", "years", "experience", "role",
    "team", "responsible", "requirements", "skills", "apply", "company", "including",
    "across", "ensure", "candidate", "position", "preferred", "qualifications"
  )
  return word in stopwords
}
