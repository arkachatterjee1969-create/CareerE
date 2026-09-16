package com.example.ui.editor

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.example.model.*
import com.example.util.InputValidator
import com.example.ui.theme.*

@Composable
fun PersonalInfoCard(
  personalInfo: PersonalInfo,
  onChange: (PersonalInfo) -> Unit
) {
  Card(
    modifier = Modifier.fillMaxWidth(),
    colors = CardDefaults.cardColors(containerColor = Color.White),
    border = BorderStroke(1.dp, WarmStoneBorder),
    shape = RoundedCornerShape(12.dp)
  ) {
    Column(modifier = Modifier.padding(18.dp)) {
      Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(Icons.Default.Person, contentDescription = null, tint = BurgundyPrimary, modifier = Modifier.size(20.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Text("Personal Details", style = MaterialTheme.typography.titleMedium, color = NearBlack)
      }

      Spacer(modifier = Modifier.height(14.dp))

      EditorialTextField(
        value = personalInfo.fullName,
        onValueChange = { onChange(personalInfo.copy(fullName = it)) },
        label = "Full Name *",
        placeholder = "e.g. Alexandre Laurent"
      )

      Spacer(modifier = Modifier.height(10.dp))

      EditorialTextField(
        value = personalInfo.professionalTitle,
        onValueChange = { onChange(personalInfo.copy(professionalTitle = it)) },
        label = "Professional Headline / Target Role",
        placeholder = "e.g. Staff Systems & Mobile Architect"
      )

      Spacer(modifier = Modifier.height(10.dp))

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        val isEmailError = personalInfo.email.isNotBlank() && !InputValidator.isValidEmail(personalInfo.email)
        val isPhoneError = personalInfo.phone.isNotBlank() && !InputValidator.isValidPhone(personalInfo.phone)

        EditorialTextField(
          value = personalInfo.email,
          onValueChange = { onChange(personalInfo.copy(email = it)) },
          label = "Email Address *",
          placeholder = "alexandre@example.com",
          modifier = Modifier.weight(1f),
          isError = isEmailError,
          errorMessage = "Invalid email format"
        )
        EditorialTextField(
          value = personalInfo.phone,
          onValueChange = { onChange(personalInfo.copy(phone = it)) },
          label = "Phone Number",
          placeholder = "+33 6 12 34 56 78",
          modifier = Modifier.weight(1f),
          isError = isPhoneError,
          errorMessage = "Invalid phone format"
        )
      }

      Spacer(modifier = Modifier.height(10.dp))

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        EditorialTextField(
          value = personalInfo.city,
          onValueChange = { onChange(personalInfo.copy(city = it)) },
          label = "City / Region",
          placeholder = "Paris",
          modifier = Modifier.weight(1f)
        )
        EditorialTextField(
          value = personalInfo.country,
          onValueChange = { onChange(personalInfo.copy(country = it)) },
          label = "Country",
          placeholder = "France",
          modifier = Modifier.weight(1f)
        )
      }

      Spacer(modifier = Modifier.height(10.dp))

      EditorialTextField(
        value = personalInfo.linkedinUrl,
        onValueChange = { onChange(personalInfo.copy(linkedinUrl = it)) },
        label = "LinkedIn Profile URL",
        placeholder = "linkedin.com/in/alexandre-laurent"
      )
    }
  }
}

@Composable
fun SummaryCard(
  personalInfo: PersonalInfo,
  onChange: (PersonalInfo) -> Unit,
  onOpenEnhancer: (String) -> Unit
) {
  Card(
    modifier = Modifier.fillMaxWidth(),
    colors = CardDefaults.cardColors(containerColor = Color.White),
    border = BorderStroke(1.dp, WarmStoneBorder),
    shape = RoundedCornerShape(12.dp)
  ) {
    Column(modifier = Modifier.padding(18.dp)) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(Icons.Default.ShortText, contentDescription = null, tint = BurgundyPrimary, modifier = Modifier.size(20.dp))
          Spacer(modifier = Modifier.width(8.dp))
          Text(if (personalInfo.isObjective) "Career Objective" else "Professional Summary", style = MaterialTheme.typography.titleMedium, color = NearBlack)
        }

        Row {
          TextButton(
            onClick = { onChange(personalInfo.copy(isObjective = !personalInfo.isObjective)) }
          ) {
            Text(if (personalInfo.isObjective) "Switch to Summary" else "Switch to Objective", fontSize = 11.sp, color = BurgundyPrimary)
          }
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      OutlinedTextField(
        value = personalInfo.summary,
        onValueChange = { onChange(personalInfo.copy(summary = it)) },
        modifier = Modifier
          .fillMaxWidth()
          .height(120.dp),
        placeholder = {
          Text(
            if (personalInfo.isObjective) "State your immediate target role, key transferable strengths, and value to the employer..."
            else "Summarize your career progression, core achievements, and domain expertise in 2–3 impactful sentences...",
            fontSize = 12.sp
          )
        },
        colors = OutlinedTextFieldDefaults.colors(
          focusedContainerColor = WarmIvorySurface,
          unfocusedContainerColor = WarmIvorySurface,
          focusedBorderColor = BurgundyPrimary,
          unfocusedBorderColor = WarmStoneBorder
        ),
        shape = RoundedCornerShape(8.dp)
      )

      Spacer(modifier = Modifier.height(8.dp))

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          "${personalInfo.summary.length} characters (ideal: 200–450)",
          style = MaterialTheme.typography.labelSmall,
          color = if (personalInfo.summary.length > 550) StatusWarning else TextTertiary
        )

        FilledTonalButton(
          onClick = { onOpenEnhancer(personalInfo.summary) },
          colors = ButtonDefaults.filledTonalButtonColors(containerColor = WarmStoneLight, contentColor = BurgundyPrimary),
          contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
          shape = RoundedCornerShape(6.dp)
        ) {
          Icon(Icons.Default.AutoFixHigh, contentDescription = null, modifier = Modifier.size(14.dp))
          Spacer(modifier = Modifier.width(4.dp))
          Text("Polish with Assistant", fontSize = 11.sp)
        }
      }
    }
  }
}

@Composable
fun ExperienceCard(
  experiences: List<ExperienceItem>,
  onChange: (List<ExperienceItem>) -> Unit,
  onOpenEnhancer: (String, (String) -> Unit) -> Unit
) {
  Card(
    modifier = Modifier.fillMaxWidth(),
    colors = CardDefaults.cardColors(containerColor = Color.White),
    border = BorderStroke(1.dp, WarmStoneBorder),
    shape = RoundedCornerShape(12.dp)
  ) {
    Column(modifier = Modifier.padding(18.dp)) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(Icons.Default.WorkOutline, contentDescription = null, tint = BurgundyPrimary, modifier = Modifier.size(20.dp))
          Spacer(modifier = Modifier.width(8.dp))
          Text("Work Experience (${experiences.size})", style = MaterialTheme.typography.titleMedium, color = NearBlack)
        }

        IconButton(
          onClick = {
            val newItem = ExperienceItem(jobTitle = "", company = "", startDate = "2024", endDate = "Present", isCurrent = true, bullets = listOf(""))
            onChange(listOf(newItem) + experiences)
          }
        ) {
          Icon(Icons.Default.AddCircle, contentDescription = "Add Experience", tint = BurgundyPrimary)
        }
      }

      if (experiences.isEmpty()) {
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
          contentAlignment = Alignment.Center
        ) {
          Text("No experience entries added yet. Tap + to create one.", style = MaterialTheme.typography.bodySmall, color = TextTertiary)
        }
      }

      experiences.forEachIndexed { index, exp ->
        Spacer(modifier = Modifier.height(12.dp))
        Surface(
          modifier = Modifier.fillMaxWidth(),
          color = WarmIvorySurface,
          shape = RoundedCornerShape(8.dp),
          border = BorderStroke(0.8.dp, WarmStoneBorder)
        ) {
          Column(modifier = Modifier.padding(14.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text("Position #${index + 1}", style = MaterialTheme.typography.labelSmall, color = BurgundyPrimary, fontWeight = FontWeight.Bold)
              IconButton(
                onClick = {
                  val updated = experiences.toMutableList().apply { removeAt(index) }
                  onChange(updated)
                },
                modifier = Modifier.size(24.dp)
              ) {
                Icon(Icons.Default.DeleteOutline, contentDescription = "Remove", tint = TextTertiary, modifier = Modifier.size(16.dp))
              }
            }

            EditorialTextField(
              value = exp.jobTitle,
              onValueChange = { title ->
                val updated = experiences.toMutableList()
                updated[index] = exp.copy(jobTitle = title)
                onChange(updated)
              },
              label = "Job Title",
              placeholder = "e.g. Lead Mobile Engineer"
            )

            Spacer(modifier = Modifier.height(8.dp))

            EditorialTextField(
              value = exp.company,
              onValueChange = { comp ->
                val updated = experiences.toMutableList()
                updated[index] = exp.copy(company = comp)
                onChange(updated)
              },
              label = "Company / Organization",
              placeholder = "e.g. Vesper Systems"
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
              EditorialTextField(
                value = exp.startDate,
                onValueChange = { s ->
                  val updated = experiences.toMutableList()
                  updated[index] = exp.copy(startDate = s)
                  onChange(updated)
                },
                label = "Start Date",
                placeholder = "Jan 2022",
                modifier = Modifier.weight(1f)
              )
              EditorialTextField(
                value = if (exp.isCurrent) "Present" else exp.endDate,
                onValueChange = { e ->
                  val updated = experiences.toMutableList()
                  updated[index] = exp.copy(endDate = e)
                  onChange(updated)
                },
                label = "End Date",
                placeholder = "Present",
                modifier = Modifier.weight(1f)
              )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Bullet points
            Text("Achievements & Impact Bullets", style = MaterialTheme.typography.labelSmall, color = NearBlack, fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(6.dp))

            exp.bullets.forEachIndexed { bIndex, bullet ->
              Row(
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(vertical = 3.dp),
                verticalAlignment = Alignment.CenterVertically
              ) {
                OutlinedTextField(
                  value = bullet,
                  onValueChange = { newB ->
                    val updatedBullets = exp.bullets.toMutableList().apply { set(bIndex, newB) }
                    val updated = experiences.toMutableList()
                    updated[index] = exp.copy(bullets = updatedBullets)
                    onChange(updated)
                  },
                  modifier = Modifier.weight(1f),
                  placeholder = { Text("e.g. Spearheaded high-throughput pipeline, lifting reliability by 24%...", fontSize = 11.sp) },
                  colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedBorderColor = BurgundyPrimary,
                    unfocusedBorderColor = WarmStoneBorder
                  ),
                  shape = RoundedCornerShape(6.dp)
                )

                IconButton(
                  onClick = {
                    onOpenEnhancer(bullet) { enhanced ->
                      val updatedBullets = exp.bullets.toMutableList().apply { set(bIndex, enhanced) }
                      val updated = experiences.toMutableList()
                      updated[index] = exp.copy(bullets = updatedBullets)
                      onChange(updated)
                    }
                  }
                ) {
                  Icon(Icons.Default.AutoFixHigh, contentDescription = "Enhance", tint = BurgundyPrimary, modifier = Modifier.size(18.dp))
                }

                IconButton(
                  onClick = {
                    val updatedBullets = exp.bullets.toMutableList().apply { removeAt(bIndex) }
                    val updated = experiences.toMutableList()
                    updated[index] = exp.copy(bullets = updatedBullets)
                    onChange(updated)
                  },
                  modifier = Modifier.size(24.dp)
                ) {
                  Icon(Icons.Default.Close, contentDescription = "Remove bullet", tint = TextTertiary, modifier = Modifier.size(14.dp))
                }
              }
            }

            TextButton(
              onClick = {
                val updatedBullets = exp.bullets + ""
                val updated = experiences.toMutableList()
                updated[index] = exp.copy(bullets = updatedBullets)
                onChange(updated)
              }
            ) {
              Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(14.dp))
              Spacer(modifier = Modifier.width(4.dp))
              Text("Add Bullet Point", fontSize = 11.sp, color = BurgundyPrimary)
            }
          }
        }
      }
    }
  }
}

@Composable
fun EducationCard(
  educations: List<EducationItem>,
  onChange: (List<EducationItem>) -> Unit
) {
  Card(
    modifier = Modifier.fillMaxWidth(),
    colors = CardDefaults.cardColors(containerColor = Color.White),
    border = BorderStroke(1.dp, WarmStoneBorder),
    shape = RoundedCornerShape(12.dp)
  ) {
    Column(modifier = Modifier.padding(18.dp)) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(Icons.Default.School, contentDescription = null, tint = BurgundyPrimary, modifier = Modifier.size(20.dp))
          Spacer(modifier = Modifier.width(8.dp))
          Text("Education (${educations.size})", style = MaterialTheme.typography.titleMedium, color = NearBlack)
        }

        IconButton(
          onClick = {
            val newEdu = EducationItem(degree = "B.Sc. in Computer Science", institution = "University", startDate = "2020", endDate = "2024")
            onChange(listOf(newEdu) + educations)
          }
        ) {
          Icon(Icons.Default.AddCircle, contentDescription = "Add Education", tint = BurgundyPrimary)
        }
      }

      educations.forEachIndexed { index, edu ->
        Spacer(modifier = Modifier.height(10.dp))
        Surface(
          modifier = Modifier.fillMaxWidth(),
          color = WarmIvorySurface,
          shape = RoundedCornerShape(8.dp),
          border = BorderStroke(0.8.dp, WarmStoneBorder)
        ) {
          Column(modifier = Modifier.padding(14.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              Text("Degree #${index + 1}", style = MaterialTheme.typography.labelSmall, color = BurgundyPrimary, fontWeight = FontWeight.Bold)
              IconButton(
                onClick = {
                  val updated = educations.toMutableList().apply { removeAt(index) }
                  onChange(updated)
                },
                modifier = Modifier.size(20.dp)
              ) {
                Icon(Icons.Default.DeleteOutline, contentDescription = "Remove", tint = TextTertiary, modifier = Modifier.size(16.dp))
              }
            }

            EditorialTextField(
              value = edu.degree,
              onValueChange = { d ->
                val u = educations.toMutableList().apply { set(index, edu.copy(degree = d)) }
                onChange(u)
              },
              label = "Degree / Diploma",
              placeholder = "Master of Science in Distributed Computing"
            )

            Spacer(modifier = Modifier.height(8.dp))

            EditorialTextField(
              value = edu.institution,
              onValueChange = { i ->
                val u = educations.toMutableList().apply { set(index, edu.copy(institution = i)) }
                onChange(u)
              },
              label = "Institution / University",
              placeholder = "École Polytechnique"
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
              EditorialTextField(
                value = edu.startDate,
                onValueChange = { s ->
                  val u = educations.toMutableList().apply { set(index, edu.copy(startDate = s)) }
                  onChange(u)
                },
                label = "Graduation Year",
                placeholder = "2024",
                modifier = Modifier.weight(1f)
              )
              EditorialTextField(
                value = edu.gpa,
                onValueChange = { g ->
                  val u = educations.toMutableList().apply { set(index, edu.copy(gpa = g)) }
                  onChange(u)
                },
                label = "GPA / Honors",
                placeholder = "Honors (Top 5%)",
                modifier = Modifier.weight(1f)
              )
            }
          }
        }
      }
    }
  }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SkillsCard(
  skills: List<SkillItem>,
  onChange: (List<SkillItem>) -> Unit
) {
  var newSkillName by remember { mutableStateOf("") }

  Card(
    modifier = Modifier.fillMaxWidth(),
    colors = CardDefaults.cardColors(containerColor = Color.White),
    border = BorderStroke(1.dp, WarmStoneBorder),
    shape = RoundedCornerShape(12.dp)
  ) {
    Column(modifier = Modifier.padding(18.dp)) {
      Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(Icons.Default.Terminal, contentDescription = null, tint = BurgundyPrimary, modifier = Modifier.size(20.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Text("Skills & Core Competencies (${skills.size})", style = MaterialTheme.typography.titleMedium, color = NearBlack)
      }

      Spacer(modifier = Modifier.height(12.dp))

      Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
      ) {
        OutlinedTextField(
          value = newSkillName,
          onValueChange = { newSkillName = it },
          placeholder = { Text("Add skill (e.g. Kotlin, Docker, Financial Modeling)...", fontSize = 12.sp) },
          modifier = Modifier.weight(1f),
          colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = WarmIvorySurface,
            unfocusedContainerColor = WarmIvorySurface,
            focusedBorderColor = BurgundyPrimary,
            unfocusedBorderColor = WarmStoneBorder
          ),
          shape = RoundedCornerShape(8.dp),
          singleLine = true
        )

        Spacer(modifier = Modifier.width(8.dp))

        Button(
          onClick = {
            if (newSkillName.isNotBlank()) {
              onChange(skills + SkillItem(name = newSkillName.trim()))
              newSkillName = ""
            }
          },
          colors = ButtonDefaults.buttonColors(containerColor = BurgundyPrimary),
          shape = RoundedCornerShape(8.dp)
        ) {
          Text("Add", color = WarmIvory)
        }
      }

      Spacer(modifier = Modifier.height(14.dp))

      FlowRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        skills.forEachIndexed { index, skill ->
          Surface(
            color = WarmStoneLight,
            shape = RoundedCornerShape(8.dp),
            border = BorderStroke(0.8.dp, WarmStoneBorder)
          ) {
            Row(
              modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(skill.name, style = MaterialTheme.typography.bodySmall, color = NearBlack)
              Spacer(modifier = Modifier.width(6.dp))
              IconButton(
                onClick = {
                  val u = skills.toMutableList().apply { removeAt(index) }
                  onChange(u)
                },
                modifier = Modifier.size(16.dp)
              ) {
                Icon(Icons.Default.Close, contentDescription = "Remove", tint = TextSecondary, modifier = Modifier.size(12.dp))
              }
            }
          }
        }
      }
    }
  }
}

@Composable
fun EditorialTextField(
  value: String,
  onValueChange: (String) -> Unit,
  label: String,
  placeholder: String,
  modifier: Modifier = Modifier,
  isError: Boolean = false,
  errorMessage: String? = null
) {
  OutlinedTextField(
    value = value,
    onValueChange = onValueChange,
    label = { Text(label, fontSize = 11.5.sp) },
    placeholder = { Text(placeholder, fontSize = 12.sp) },
    modifier = modifier.fillMaxWidth(),
    isError = isError,
    supportingText = {
      if (isError && errorMessage != null) {
        Text(errorMessage, color = MaterialTheme.colorScheme.error)
      }
    },
    colors = OutlinedTextFieldDefaults.colors(
      focusedContainerColor = WarmIvorySurface,
      unfocusedContainerColor = WarmIvorySurface,
      focusedBorderColor = BurgundyPrimary,
      unfocusedBorderColor = WarmStoneBorder,
      errorBorderColor = MaterialTheme.colorScheme.error,
      errorLabelColor = MaterialTheme.colorScheme.error
    ),
    shape = RoundedCornerShape(8.dp),
    singleLine = true
  )
}
