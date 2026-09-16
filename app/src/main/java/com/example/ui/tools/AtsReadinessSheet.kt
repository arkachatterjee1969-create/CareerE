package com.example.ui.tools

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.ResumeData
import com.example.template.TemplateRepository
import com.example.ui.theme.*

data class AtsAuditResult(
  val score: Int,
  val passedChecks: List<String>,
  val warningChecks: List<String>,
  val improvementTips: List<String>
)

object AtsAnalyzer {
  fun analyze(resume: ResumeData): AtsAuditResult {
    var score = 0
    val passed = mutableListOf<String>()
    val warnings = mutableListOf<String>()
    val tips = mutableListOf<String>()

    val template = TemplateRepository.getById(resume.templateId)

    // Contact checks
    if (resume.personalInfo.fullName.isNotBlank()) {
      score += 15
      passed.add("Full candidate name present and prominently indexed")
    } else {
      warnings.add("Candidate name is missing")
    }

    if (resume.personalInfo.email.isNotBlank() && resume.personalInfo.phone.isNotBlank()) {
      score += 15
      passed.add("Direct contact channels (Email & Phone) verified")
    } else {
      warnings.add("Email or Phone missing — primary recruiter blocker")
    }

    if (resume.personalInfo.city.isNotBlank() || resume.personalInfo.country.isNotBlank()) {
      score += 10
      passed.add("Geographic location/jurisdiction specified")
    } else {
      tips.add("Include city and country for regional ATS candidate filtering")
    }

    // Headline & Summary
    if (resume.personalInfo.professionalTitle.isNotBlank()) {
      score += 10
      passed.add("Target job title matches hiring search queries")
    } else {
      tips.add("Add a targeted Professional Title (e.g. 'Senior Systems Architect')")
    }

    if (resume.personalInfo.summary.isNotBlank()) {
      score += 15
      if (resume.personalInfo.summary.length > 700) {
        warnings.add("Summary is lengthy (${resume.personalInfo.summary.length} chars) — recommend 200–450 chars")
      } else {
        passed.add("Executive summary is concise and keyword-dense")
      }
    } else {
      tips.add("Add a 2–3 sentence executive summary to highlight your value proposition")
    }

    // Experience
    if (resume.experiences.isNotEmpty()) {
      score += 20
      passed.add("${resume.experiences.size} chronological experience entries found")

      // Check for quantified metrics in bullets
      val allBullets = resume.experiences.flatMap { it.bullets }
      val metricBullets = allBullets.filter { it.contains(Regex("[0-9]|%|\\$|€")) }
      if (metricBullets.isNotEmpty()) {
        score += 5
        passed.add("Quantified achievements detected (KPIs, %, or revenue metrics)")
      } else {
        tips.add("Include measurable outcomes in your bullets (e.g., 'reduced latency by 35%')")
      }
    } else {
      warnings.add("No experience entries added yet")
    }

    // Education
    if (resume.educations.isNotEmpty()) {
      score += 10
      passed.add("Academic credentials and degrees clearly documented")
    } else {
      tips.add("Add formal degree or relevant certifications")
    }

    // Skills
    if (resume.skills.size >= 5) {
      score += 10
      passed.add("Strong skill portfolio (${resume.skills.size} technical/soft skills)")
    } else if (resume.skills.isNotEmpty()) {
      score += 5
      tips.add("Expand skill inventory to at least 6–8 targeted competencies")
    } else {
      warnings.add("Skills section is empty — crucial for keyword indexers")
    }

    // Template ATS check
    if (template.isAtsSafe) {
      score = minOf(100, score + 5)
      passed.add("Active template '${template.name}' is validated ATS-safe format")
    } else {
      tips.add("Template '${template.name}' is creative — consider 'ATS Classic' for conservative corporate portals")
    }

    return AtsAuditResult(
      score = minOf(100, score),
      passedChecks = passed,
      warningChecks = warnings,
      improvementTips = tips
    )
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AtsReadinessSheet(
  resume: ResumeData,
  onDismiss: () -> Unit
) {
  val audit = remember(resume) { AtsAnalyzer.analyze(resume) }

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
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Text("ATS Readiness Audit", style = MaterialTheme.typography.titleLarge, color = NearBlack)
          Text(
            "Designed with ATS-friendly formatting principles",
            style = MaterialTheme.typography.bodySmall,
            color = TextSecondary
          )
        }

        Surface(
          color = if (audit.score >= 85) StatusSuccess.copy(alpha = 0.15f) else StatusWarning.copy(alpha = 0.15f),
          shape = RoundedCornerShape(12.dp),
          border = BorderStroke(1.dp, if (audit.score >= 85) StatusSuccess else StatusWarning)
        ) {
          Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              "${audit.score}",
              fontSize = 20.sp,
              fontWeight = FontWeight.Bold,
              color = if (audit.score >= 85) StatusSuccess else StatusWarning
            )
            Text("/100", fontSize = 12.sp, color = TextSecondary)
          }
        }
      }

      Spacer(modifier = Modifier.height(20.dp))

      // Passed Audits
      Text("Verified Formatting (${audit.passedChecks.size})", style = MaterialTheme.typography.titleSmall, color = NearBlack)
      Spacer(modifier = Modifier.height(8.dp))
      for (item in audit.passedChecks) {
        Row(
          modifier = Modifier.padding(vertical = 4.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Icon(Icons.Default.CheckCircle, contentDescription = null, tint = StatusSuccess, modifier = Modifier.size(16.dp))
          Spacer(modifier = Modifier.width(8.dp))
          Text(item, style = MaterialTheme.typography.bodySmall, color = NearBlack)
        }
      }

      if (audit.warningChecks.isNotEmpty()) {
        Spacer(modifier = Modifier.height(16.dp))
        Text("Attention Needed (${audit.warningChecks.size})", style = MaterialTheme.typography.titleSmall, color = StatusWarning)
        Spacer(modifier = Modifier.height(8.dp))
        for (item in audit.warningChecks) {
          Row(
            modifier = Modifier.padding(vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(Icons.Default.Warning, contentDescription = null, tint = StatusWarning, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(item, style = MaterialTheme.typography.bodySmall, color = NearBlack)
          }
        }
      }

      if (audit.improvementTips.isNotEmpty()) {
        Spacer(modifier = Modifier.height(16.dp))
        Text("Strategic Recommendations", style = MaterialTheme.typography.titleSmall, color = BurgundyPrimary)
        Spacer(modifier = Modifier.height(8.dp))
        for (tip in audit.improvementTips) {
          Row(
            modifier = Modifier.padding(vertical = 4.dp),
            verticalAlignment = Alignment.Top
          ) {
            Text("•", color = BurgundyPrimary, fontWeight = FontWeight.Bold, modifier = Modifier.padding(end = 8.dp))
            Text(tip, style = MaterialTheme.typography.bodySmall, color = TextSecondary)
          }
        }
      }

      Spacer(modifier = Modifier.height(24.dp))

      Button(
        onClick = onDismiss,
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(containerColor = BurgundyPrimary)
      ) {
        Text("Return to Editor", color = WarmIvory)
      }
    }
  }
}
