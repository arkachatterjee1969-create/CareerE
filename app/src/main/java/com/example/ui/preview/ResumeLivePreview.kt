package com.example.ui.preview

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.PageSize
import com.example.model.ResumeData
import com.example.model.ResumeSettings
import com.example.util.InputValidator
import com.example.template.*
import com.example.ui.theme.*

@Composable
fun ResumeLivePreview(
  resume: ResumeData,
  modifier: Modifier = Modifier,
  zoomScale: Float = 1.0f
) {
  val template = TemplateRepository.getById(resume.templateId)

  val primaryAccent = try {
    Color(android.graphics.Color.parseColor(resume.settings.primaryColorHex))
  } catch (e: Exception) {
    BurgundyPrimary
  }

  val secondaryAccent = try {
    Color(android.graphics.Color.parseColor(template.secondaryColorHex))
  } catch (e: Exception) {
    MutedRose
  }

  val headingFont = when (resume.settings.fontChoice) {
    "Playfair Display / Serif" -> FontFamily.Serif
    "Fira Code / Mono" -> FontFamily.Monospace
    else -> FontFamily.SansSerif
  }

  val bodyFont = when (resume.settings.fontChoice) {
    "Fira Code / Mono" -> FontFamily.Monospace
    else -> FontFamily.SansSerif
  }

  // Document Container styled like high-end European ivory paper
  Box(
    modifier = modifier
      .fillMaxWidth()
      .padding(16.dp),
    contentAlignment = Alignment.TopCenter
  ) {
    Surface(
      modifier = Modifier
        .widthIn(max = 680.dp)
        .fillMaxWidth()
        .shadow(8.dp, RoundedCornerShape(2.dp))
        .background(Color.White)
        .border(1.dp, WarmStoneBorder, RoundedCornerShape(2.dp)),
      color = Color.White
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(
            horizontal = when (template.layout) {
              LayoutStructure.BORDERED_MINIMAL -> 20.dp
              else -> 28.dp
            },
            vertical = 28.dp
          )
      ) {
        // Layout 1: HEADER_ACCENT
        if (template.layout == LayoutStructure.HEADER_ACCENT) {
          HeaderAccentBlock(resume, primaryAccent, headingFont, bodyFont)
          Spacer(modifier = Modifier.height(20.dp))
        } else {
          // Standard Top Header
          StandardHeader(resume, primaryAccent, headingFont, bodyFont, template)
          Spacer(modifier = Modifier.height(18.dp))
        }

        // Layout Routing
        when (template.layout) {
          LayoutStructure.TWO_COLUMN_LEFT, LayoutStructure.SPLIT_PANEL -> {
            TwoColumnLayout(resume, primaryAccent, secondaryAccent, headingFont, bodyFont, isSidebarLeft = true, template = template)
          }
          LayoutStructure.TWO_COLUMN_RIGHT -> {
            TwoColumnLayout(resume, primaryAccent, secondaryAccent, headingFont, bodyFont, isSidebarLeft = false, template = template)
          }
          LayoutStructure.MODERN_CARD -> {
            CardBasedLayout(resume, primaryAccent, secondaryAccent, headingFont, bodyFont, template = template)
          }
          else -> {
            // Default SINGLE_COLUMN & BORDERED_MINIMAL
            SingleColumnLayout(resume, primaryAccent, secondaryAccent, headingFont, bodyFont, template = template)
          }
        }
      }
    }
  }
}

@Composable
private fun StandardHeader(
  resume: ResumeData,
  accentColor: Color,
  headingFont: FontFamily,
  bodyFont: FontFamily,
  template: TemplateDefinition
) {
  Column(modifier = Modifier.fillMaxWidth()) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Column(modifier = Modifier.weight(1f)) {
        val name = resume.personalInfo.fullName.ifBlank { "Candidate Name" }
        Text(
          text = name,
          fontSize = 24.sp,
          fontFamily = headingFont,
          fontWeight = FontWeight.Bold,
          color = accentColor,
          letterSpacing = (-0.3).sp
        )

        if (resume.personalInfo.professionalTitle.isNotBlank()) {
          Text(
            text = resume.personalInfo.professionalTitle,
            fontSize = 13.sp,
            fontFamily = bodyFont,
            fontWeight = FontWeight.Medium,
            color = TextSecondary,
            modifier = Modifier.padding(top = 2.dp)
          )
        }
      }

      if (template.supportsPhoto && resume.settings.showPhoto) {
        Surface(
          modifier = Modifier.size(52.dp),
          shape = CircleShape,
          color = WarmStoneLight,
          border = androidx.compose.foundation.BorderStroke(1.dp, accentColor)
        ) {
          Icon(
            Icons.Default.Person,
            contentDescription = "Profile Photo",
            tint = accentColor,
            modifier = Modifier.padding(10.dp)
          )
        }
      }
    }

    Spacer(modifier = Modifier.height(10.dp))

    // Contact Information line
    val contacts = mutableListOf<String>()
    if (resume.personalInfo.email.isNotBlank()) contacts.add(resume.personalInfo.email)
    if (resume.personalInfo.phone.isNotBlank()) contacts.add(resume.personalInfo.phone)
    val loc = listOf(resume.personalInfo.city, resume.personalInfo.country).filter { it.isNotBlank() }.joinToString(", ")
    if (loc.isNotBlank()) contacts.add(loc)
    if (resume.personalInfo.linkedinUrl.isNotBlank()) contacts.add(resume.personalInfo.linkedinUrl)

    if (contacts.isNotEmpty()) {
      Text(
        text = contacts.joinToString("  •  "),
        fontSize = 11.sp,
        fontFamily = bodyFont,
        color = NearBlack,
        lineHeight = 16.sp
      )
    }

    Spacer(modifier = Modifier.height(12.dp))
    RenderDivider(template, accentColor)
  }
}

@Composable
private fun HeaderAccentBlock(
  resume: ResumeData,
  accentColor: Color,
  headingFont: FontFamily,
  bodyFont: FontFamily
) {
  Box(
    modifier = Modifier
      .fillMaxWidth()
      .background(accentColor, RoundedCornerShape(4.dp))
      .padding(18.dp)
  ) {
    Column {
      Text(
        text = resume.personalInfo.fullName.ifBlank { "Candidate Name" },
        fontSize = 24.sp,
        fontFamily = headingFont,
        fontWeight = FontWeight.Bold,
        color = WarmIvory
      )
      if (resume.personalInfo.professionalTitle.isNotBlank()) {
        Text(
          text = resume.personalInfo.professionalTitle,
          fontSize = 13.sp,
          fontFamily = bodyFont,
          color = WarmStone,
          modifier = Modifier.padding(top = 2.dp)
        )
      }

      val contacts = listOfNotNull(
        resume.personalInfo.email.takeIf { it.isNotBlank() },
        resume.personalInfo.phone.takeIf { it.isNotBlank() },
        listOf(resume.personalInfo.city, resume.personalInfo.country).filter { it.isNotBlank() }.joinToString(", ").takeIf { it.isNotBlank() }
      ).joinToString("  |  ")

      if (contacts.isNotBlank()) {
        Spacer(modifier = Modifier.height(8.dp))
        Text(
          text = contacts,
          fontSize = 10.sp,
          fontFamily = bodyFont,
          color = WarmIvory.copy(alpha = 0.85f)
        )
      }
    }
  }
}

@Composable
private fun RenderDivider(template: TemplateDefinition, accentColor: Color) {
  when (template.dividerStyle) {
    DividerStyle.SOLID_BAR -> {
      Box(modifier = Modifier.fillMaxWidth().height(2.5.dp).background(accentColor))
    }
    DividerStyle.DOUBLE_LINE -> {
      Column(modifier = Modifier.fillMaxWidth()) {
        HorizontalDivider(thickness = 1.dp, color = accentColor)
        Spacer(modifier = Modifier.height(2.dp))
        HorizontalDivider(thickness = 0.5.dp, color = accentColor.copy(alpha = 0.5f))
      }
    }
    DividerStyle.ACCENT_DOT -> {
      Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
      ) {
        HorizontalDivider(modifier = Modifier.weight(1f), color = WarmStoneBorder)
        Box(modifier = Modifier.padding(horizontal = 6.dp).size(4.dp).background(accentColor, CircleShape))
        HorizontalDivider(modifier = Modifier.weight(1f), color = WarmStoneBorder)
      }
    }
    DividerStyle.HAIRLINE -> {
      HorizontalDivider(thickness = 0.8.dp, color = WarmStoneBorder)
    }
    DividerStyle.NONE -> {
      // Clean spacing only
    }
  }
}

@Composable
private fun SingleColumnLayout(
  resume: ResumeData,
  accentColor: Color,
  secondaryColor: Color,
  headingFont: FontFamily,
  bodyFont: FontFamily,
  template: TemplateDefinition
) {
  Column(
    modifier = Modifier.fillMaxWidth(),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    // Summary
    if (resume.personalInfo.summary.isNotBlank()) {
      PreviewSectionHeader(if (resume.personalInfo.isObjective) "CAREER OBJECTIVE" else "PROFESSIONAL SUMMARY", accentColor, headingFont, template)
      Text(
        text = resume.personalInfo.summary,
        fontSize = 11.5.sp,
        fontFamily = bodyFont,
        color = NearBlack,
        lineHeight = 17.sp
      )
    }

    // Work Experience
    if (resume.experiences.isNotEmpty()) {
      PreviewSectionHeader("WORK EXPERIENCE", accentColor, headingFont, template)
      Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        resume.experiences.forEach { exp ->
          Column {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.Top
            ) {
              Text(
                text = "${exp.jobTitle.ifBlank { "Role" }}  —  ${exp.company.ifBlank { "Company" }}",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = bodyFont,
                color = NearBlack,
                modifier = Modifier.weight(1f)
              )
              val date = "${exp.startDate} – ${if (exp.isCurrent) "Present" else exp.endDate}".trim()
              Text(
                text = date,
                fontSize = 10.5.sp,
                fontFamily = bodyFont,
                color = TextSecondary
              )
            }

            if (exp.location.isNotBlank()) {
              Text(
                text = exp.location,
                fontSize = 10.5.sp,
                fontFamily = bodyFont,
                color = TextTertiary,
                modifier = Modifier.padding(bottom = 4.dp)
              )
            }

            exp.bullets.forEach { bullet ->
              if (bullet.isNotBlank()) {
                Row(modifier = Modifier.padding(vertical = 1.5.dp)) {
                  Text("• ", fontSize = 11.sp, color = accentColor)
                  Text(
                    text = bullet,
                    fontSize = 11.sp,
                    fontFamily = bodyFont,
                    color = NearBlack,
                    lineHeight = 16.sp
                  )
                }
              }
            }
          }
        }
      }
    }

    // Education
    if (resume.educations.isNotEmpty()) {
      PreviewSectionHeader("EDUCATION", accentColor, headingFont, template)
      Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        resume.educations.forEach { edu ->
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Column(modifier = Modifier.weight(1f)) {
              val degree = listOf(edu.degree, edu.fieldOfStudy).filter { it.isNotBlank() }.joinToString(", ")
              Text(
                text = degree.ifBlank { "Degree" },
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = bodyFont,
                color = NearBlack
              )
              val inst = listOf(edu.institution, edu.location, edu.gpa).filter { it.isNotBlank() }.joinToString(" • ")
              Text(
                text = inst,
                fontSize = 10.5.sp,
                fontFamily = bodyFont,
                color = TextSecondary
              )
            }
            Text(
              text = "${edu.startDate} – ${if (edu.isCurrent) "Present" else edu.endDate}",
              fontSize = 10.5.sp,
              fontFamily = bodyFont,
              color = TextSecondary
            )
          }
        }
      }
    }

    // Skills
    if (resume.skills.isNotEmpty()) {
      PreviewSectionHeader("CORE SKILLS & COMPETENCIES", accentColor, headingFont, template)
      SkillsRow(resume.skills, accentColor, bodyFont)
    }

    // Projects
    if (resume.projects.isNotEmpty()) {
      PreviewSectionHeader("KEY PROJECTS", accentColor, headingFont, template)
      Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        resume.projects.forEach { proj ->
          Column {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              Text(
                text = "${proj.name}  (${proj.role})",
                fontSize = 11.5.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = bodyFont,
                color = NearBlack
              )
              if (proj.date.isNotBlank()) {
                Text(proj.date, fontSize = 10.5.sp, fontFamily = bodyFont, color = TextSecondary)
              }
            }
            if (proj.description.isNotBlank()) {
              Text(
                proj.description,
                fontSize = 11.sp,
                fontFamily = bodyFont,
                color = NearBlack,
                lineHeight = 15.sp
              )
            }
          }
        }
      }
    }

    // Languages
    if (resume.languages.isNotEmpty()) {
      PreviewSectionHeader("LANGUAGES", accentColor, headingFont, template)
      Text(
        text = resume.languages.joinToString("   •   ") { "${it.language} (${it.proficiency})" },
        fontSize = 11.sp,
        fontFamily = bodyFont,
        color = NearBlack
      )
    }
  }
}

@Composable
private fun TwoColumnLayout(
  resume: ResumeData,
  accentColor: Color,
  secondaryColor: Color,
  headingFont: FontFamily,
  bodyFont: FontFamily,
  isSidebarLeft: Boolean,
  template: TemplateDefinition
) {
  Row(
    modifier = Modifier.fillMaxWidth(),
    horizontalArrangement = Arrangement.spacedBy(20.dp)
  ) {
    val sidebarContent = @Composable {
      Column(
        modifier = Modifier
          .weight(0.35f)
          .padding(top = 4.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
      ) {
        if (resume.skills.isNotEmpty()) {
          PreviewSectionHeader("SKILLS", accentColor, headingFont, template)
          Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            resume.skills.forEach { skill ->
              Text("• ${skill.name}", fontSize = 10.5.sp, fontFamily = bodyFont, color = NearBlack)
            }
          }
        }

        if (resume.educations.isNotEmpty()) {
          PreviewSectionHeader("EDUCATION", accentColor, headingFont, template)
          resume.educations.forEach { edu ->
            Column(modifier = Modifier.padding(bottom = 6.dp)) {
              Text(edu.degree, fontSize = 11.sp, fontWeight = FontWeight.Bold, fontFamily = bodyFont, color = NearBlack)
              Text(edu.institution, fontSize = 10.sp, color = TextSecondary, fontFamily = bodyFont)
              Text("${edu.startDate} – ${edu.endDate}", fontSize = 9.5.sp, color = TextTertiary, fontFamily = bodyFont)
            }
          }
        }

        if (resume.languages.isNotEmpty()) {
          PreviewSectionHeader("LANGUAGES", accentColor, headingFont, template)
          resume.languages.forEach { lang ->
            Text("${lang.language} - ${lang.proficiency}", fontSize = 10.sp, fontFamily = bodyFont, color = NearBlack)
          }
        }
      }
    }

    val mainContent = @Composable {
      Column(
        modifier = Modifier.weight(0.65f),
        verticalArrangement = Arrangement.spacedBy(16.dp)
      ) {
        if (resume.personalInfo.summary.isNotBlank()) {
          PreviewSectionHeader("PROFILE", accentColor, headingFont, template)
          Text(resume.personalInfo.summary, fontSize = 11.sp, fontFamily = bodyFont, color = NearBlack, lineHeight = 16.sp)
        }

        if (resume.experiences.isNotEmpty()) {
          PreviewSectionHeader("EXPERIENCE", accentColor, headingFont, template)
          resume.experiences.forEach { exp ->
            Column(modifier = Modifier.padding(bottom = 8.dp)) {
              Text("${exp.jobTitle} • ${exp.company}", fontSize = 11.5.sp, fontWeight = FontWeight.Bold, fontFamily = bodyFont, color = NearBlack)
              Text("${exp.startDate} – ${if (exp.isCurrent) "Present" else exp.endDate}", fontSize = 10.sp, color = TextSecondary, fontFamily = bodyFont)
              exp.bullets.forEach { b ->
                if (b.isNotBlank()) Text("• $b", fontSize = 10.5.sp, fontFamily = bodyFont, color = NearBlack, lineHeight = 15.sp)
              }
            }
          }
        }

        if (resume.projects.isNotEmpty()) {
          PreviewSectionHeader("PROJECTS", accentColor, headingFont, template)
          resume.projects.forEach { p ->
            Column(modifier = Modifier.padding(bottom = 6.dp)) {
              Text("${p.name} (${p.role})", fontSize = 11.sp, fontWeight = FontWeight.Bold, fontFamily = bodyFont, color = NearBlack)
              Text(p.description, fontSize = 10.sp, fontFamily = bodyFont, color = NearBlack)
            }
          }
        }
      }
    }

    if (isSidebarLeft) {
      sidebarContent()
      mainContent()
    } else {
      mainContent()
      sidebarContent()
    }
  }
}

@Composable
private fun CardBasedLayout(
  resume: ResumeData,
  accentColor: Color,
  secondaryColor: Color,
  headingFont: FontFamily,
  bodyFont: FontFamily,
  template: TemplateDefinition
) {
  Column(
    modifier = Modifier.fillMaxWidth(),
    verticalArrangement = Arrangement.spacedBy(14.dp)
  ) {
    if (resume.personalInfo.summary.isNotBlank()) {
      Surface(
        shape = RoundedCornerShape(6.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, WarmStoneBorder),
        color = WarmIvorySurface
      ) {
        Column(modifier = Modifier.padding(12.dp)) {
          PreviewSectionHeader("SUMMARY", accentColor, headingFont, template)
          Text(resume.personalInfo.summary, fontSize = 11.sp, fontFamily = bodyFont, color = NearBlack, lineHeight = 16.sp)
        }
      }
    }

    if (resume.experiences.isNotEmpty()) {
      PreviewSectionHeader("EXPERIENCE", accentColor, headingFont, template)
      resume.experiences.forEach { exp ->
        Surface(
          shape = RoundedCornerShape(6.dp),
          border = androidx.compose.foundation.BorderStroke(1.dp, WarmStoneBorder),
          color = Color.White
        ) {
          Column(modifier = Modifier.padding(12.dp)) {
            Text("${exp.jobTitle} @ ${exp.company}", fontSize = 12.sp, fontWeight = FontWeight.Bold, fontFamily = bodyFont, color = NearBlack)
            Text("${exp.startDate} – ${if (exp.isCurrent) "Present" else exp.endDate}", fontSize = 10.sp, color = TextSecondary)
            exp.bullets.forEach { b ->
              if (b.isNotBlank()) Text("• $b", fontSize = 10.5.sp, fontFamily = bodyFont, color = NearBlack)
            }
          }
        }
      }
    }
  }
}

@Composable
private fun PreviewSectionHeader(
  title: String,
  accentColor: Color,
  headingFont: FontFamily,
  template: TemplateDefinition
) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .padding(vertical = 4.dp),
    verticalAlignment = Alignment.CenterVertically
  ) {
    Text(
      text = title,
      fontSize = 11.5.sp,
      fontFamily = headingFont,
      fontWeight = FontWeight.Bold,
      color = accentColor,
      letterSpacing = 0.5.sp
    )
    Spacer(modifier = Modifier.width(8.dp))
    HorizontalDivider(
      modifier = Modifier.weight(1f),
      color = if (template.dividerStyle == DividerStyle.SOLID_BAR) accentColor else WarmStoneBorder,
      thickness = if (template.dividerStyle == DividerStyle.SOLID_BAR) 1.5.dp else 0.8.dp
    )
  }
}

@Composable
private fun SkillsRow(skills: List<com.example.model.SkillItem>, accentColor: Color, bodyFont: FontFamily) {
  Row(
    modifier = Modifier.fillMaxWidth(),
    horizontalArrangement = Arrangement.spacedBy(6.dp)
  ) {
    val skillText = skills.joinToString("   •   ") { it.name }
    Text(
      text = skillText,
      fontSize = 11.sp,
      fontFamily = bodyFont,
      color = NearBlack,
      lineHeight = 16.sp
    )
  }
}
