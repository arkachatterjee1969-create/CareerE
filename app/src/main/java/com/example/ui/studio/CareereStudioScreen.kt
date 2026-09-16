package com.example.ui.studio

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.brand.CareereHorizontalLogo
import com.example.export.PdfExportManager
import com.example.model.PageSize
import com.example.model.ResumeData
import com.example.model.SampleProfiles
import com.example.template.TemplateRepository
import com.example.ui.editor.*
import com.example.ui.editor.ThemeCustomizationCard
import com.example.data.DatabaseProvider
import com.example.data.ResumeRepository
import com.example.data.toEntity
import com.example.data.toResumeData
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import com.example.ui.gallery.TemplateGalleryScreen
import com.example.ui.preview.ResumeLivePreview
import com.example.ui.theme.*
import com.example.ui.tools.*



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CareereStudioScreen(
  onNavigateToBrandIdentity: () -> Unit = {}
) {
  val context = LocalContext.current

  var resume by remember { mutableStateOf<ResumeData>(SampleProfiles.softwareEngineer) }
  var activeViewTab by remember { mutableIntStateOf(0) } // 0: Editor, 1: Live Preview

  // Dialogs & Sheets
  var showTemplateGallery by remember { mutableStateOf(false) }
  var showAtsAudit by remember { mutableStateOf(false) }
  var showJobMatcher by remember { mutableStateOf(false) }
  var showProfileMenu by remember { mutableStateOf(false) }
  var showLivePreviewDialog by remember { mutableStateOf(false) }

  // Enhancer Sheet State
  var enhancerInitialText by remember { mutableStateOf<String?>(null) }
  var enhancerApplyCallback by remember { mutableStateOf<((String) -> Unit)?>(null) }

  // Auto-Save Mechanism
  val db = remember { DatabaseProvider.getDatabase(context) }
  val repository = remember { ResumeRepository(db.resumeDao()) }
  
  var isInitialLoadDone by remember { mutableStateOf(false) }

  LaunchedEffect(Unit) {
      try {
          val savedResumes = repository.allResumes.firstOrNull()
          if (!savedResumes.isNullOrEmpty()) {
              resume = savedResumes.first().toResumeData()
          }
      } catch (e: Exception) {
          // ignore
      } finally {
          isInitialLoadDone = true
      }
  }

  LaunchedEffect(resume, isInitialLoadDone) {
      if (isInitialLoadDone) {
          delay(1000)
          repository.insert(resume.toEntity())
      }
  }

  // Progress Tracker
  val completionProgress = remember(resume) {
      var filled = 0
      val totalFields = 5
      if (resume.personalInfo.fullName.isNotBlank()) filled++
      if (resume.personalInfo.email.isNotBlank()) filled++
      if (resume.personalInfo.phone.isNotBlank()) filled++
      if (resume.experiences.isNotEmpty()) filled++
      if (resume.educations.isNotEmpty()) filled++
      filled.toFloat() / totalFields
  }

  val template = remember(resume.templateId) { TemplateRepository.getById(resume.templateId) }
  val atsScore = remember(resume) { AtsAnalyzer.analyze(resume).score }

  Scaffold(
    topBar = {
      TopAppBar(
        title = {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.padding(end = 12.dp)) {
                LinearProgressIndicator(
                    progress = { completionProgress },
                    modifier = Modifier.width(60.dp).height(4.dp),
                    color = BurgundyPrimary,
                    trackColor = WarmStoneLight
                )
            }
            CareereHorizontalLogo(iconSize = 28.dp, fontSize = 19.sp)
            Spacer(modifier = Modifier.width(10.dp))
            Surface(
              color = StatusSuccess.copy(alpha = 0.12f),
              shape = RoundedCornerShape(4.dp)
            ) {
              Text(
                "Saved just now",
                style = MaterialTheme.typography.labelSmall,
                color = StatusSuccess,
                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
              )
            }
          }
        },
        actions = {
          // Brand Showcase Navigation
          IconButton(onClick = onNavigateToBrandIdentity) {
            Icon(Icons.Default.Palette, contentDescription = "Brand Showcase", tint = BurgundyPrimary)
          }

          // Sample Profiles Switcher
          Box {
            IconButton(onClick = { showProfileMenu = true }) {
              Icon(Icons.Default.SwitchAccount, contentDescription = "Sample Profiles", tint = NearBlack)
            }
            DropdownMenu(
              expanded = showProfileMenu,
              onDismissRequest = { showProfileMenu = false }
            ) {
              DropdownMenuItem(
                text = { Text("Alexandre (Staff Architect)") },
                onClick = {
                  resume = SampleProfiles.softwareEngineer.copy(templateId = resume.templateId)
                  showProfileMenu = false
                }
              )
              DropdownMenuItem(
                text = { Text("Elena (Executive Leader)") },
                onClick = {
                  resume = SampleProfiles.executiveLeader.copy(templateId = resume.templateId)
                  showProfileMenu = false
                }
              )
              DropdownMenuItem(
                text = { Text("Camille (Student / Fresher)") },
                onClick = {
                  resume = SampleProfiles.studentFresher.copy(templateId = resume.templateId)
                  showProfileMenu = false
                }
              )
            }
          }

          // Template Switcher Button
          FilledTonalButton(
            onClick = { showTemplateGallery = true },
            colors = ButtonDefaults.filledTonalButtonColors(containerColor = WarmStoneLight, contentColor = BurgundyPrimary),
            shape = RoundedCornerShape(8.dp),
            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
          ) {
            Icon(Icons.Default.DashboardCustomize, contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text(template.name, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
          }

          Spacer(modifier = Modifier.width(8.dp))

          // Export PDF Primary Action
          val coroutineScope = rememberCoroutineScope()
          Button(
            onClick = {
              coroutineScope.launch(kotlinx.coroutines.Dispatchers.IO) {
                  try {
                    val pdfFile = PdfExportManager.generatePdf(context, resume)
                    launch(kotlinx.coroutines.Dispatchers.Main) {
                        PdfExportManager.sharePdf(context, pdfFile)
                        Toast.makeText(context, "CV exported: ${pdfFile.name}", Toast.LENGTH_SHORT).show()
                    }
                  } catch (e: Exception) {
                    launch(kotlinx.coroutines.Dispatchers.Main) {
                        Toast.makeText(context, "Export error: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
                    }
                  }
              }
            },
            colors = ButtonDefaults.buttonColors(containerColor = BurgundyPrimary),
            shape = RoundedCornerShape(8.dp),
            contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp)
          ) {
            Icon(Icons.Default.Download, contentDescription = null, modifier = Modifier.size(16.dp), tint = WarmIvory)
            Spacer(modifier = Modifier.width(6.dp))
            Text("Download PDF", color = WarmIvory, fontSize = 12.sp, fontWeight = FontWeight.Bold)
          }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = WarmIvory)
      )
    },
    bottomBar = {
      // Bottom Tools Bar
      Surface(
        color = WarmIvory,
        border = androidx.compose.foundation.BorderStroke(1.dp, WarmStoneBorder)
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          // Tab Mode: Editor vs Preview
          Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            FilterChip(
              selected = activeViewTab == 0,
              onClick = { activeViewTab = 0 },
              label = { Text("Editor Form", fontSize = 11.5.sp) },
              leadingIcon = { Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(14.dp)) }
            )
            FilterChip(
              selected = activeViewTab == 1,
              onClick = { activeViewTab = 1 },
              label = { Text("Live Preview", fontSize = 11.5.sp) },
              leadingIcon = { Icon(Icons.Default.Visibility, contentDescription = null, modifier = Modifier.size(14.dp)) }
            )
          }

          // Intelligence Actions: ATS & Job Matcher
          Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedButton(
              onClick = { showAtsAudit = true },
              shape = RoundedCornerShape(6.dp),
              border = androidx.compose.foundation.BorderStroke(1.dp, BurgundyPrimary),
              colors = ButtonDefaults.outlinedButtonColors(contentColor = BurgundyPrimary),
              contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
            ) {
              Text("ATS: $atsScore%", fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }

            OutlinedButton(
              onClick = { showJobMatcher = true },
              shape = RoundedCornerShape(6.dp),
              border = androidx.compose.foundation.BorderStroke(1.dp, WarmStoneBorder),
              colors = ButtonDefaults.outlinedButtonColors(contentColor = NearBlack),
              contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
            ) {
              Icon(Icons.Default.Search, contentDescription = null, modifier = Modifier.size(14.dp))
              Spacer(modifier = Modifier.width(4.dp))
              Text("Match JD", fontSize = 11.sp)
            }
            if (activeViewTab == 0) {
              FilledTonalButton(
                onClick = { showLivePreviewDialog = true },
                shape = RoundedCornerShape(6.dp),
                colors = ButtonDefaults.filledTonalButtonColors(containerColor = BurgundyPrimary, contentColor = WarmIvory),
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
              ) {
                Icon(Icons.Default.OpenInNew, contentDescription = null, modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Live View", fontSize = 11.sp)
              }
            }
          }
        }
      }
    },
    containerColor = WarmIvory
  ) { padding ->
    Box(
      modifier = Modifier
        .fillMaxSize()
        .padding(padding)
    ) {
      if (activeViewTab == 0) {
        // Form Editor Mode
        Column(
          modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
          verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
          ThemeCustomizationCard(
            settings = resume.settings,
            onChange = { resume = resume.copy(settings = it) }
          )
          PersonalInfoCard(
            personalInfo = resume.personalInfo,
            onChange = { resume = resume.copy(personalInfo = it) }
          )

          SummaryCard(
            personalInfo = resume.personalInfo,
            onChange = { resume = resume.copy(personalInfo = it) },
            onOpenEnhancer = { text ->
              enhancerInitialText = text
              enhancerApplyCallback = { enhanced ->
                resume = resume.copy(personalInfo = resume.personalInfo.copy(summary = enhanced))
              }
            }
          )

          ExperienceCard(
            experiences = resume.experiences,
            onChange = { resume = resume.copy(experiences = it) },
            onOpenEnhancer = { text, onApply ->
              enhancerInitialText = text
              enhancerApplyCallback = onApply
            }
          )

          EducationCard(
            educations = resume.educations,
            onChange = { resume = resume.copy(educations = it) }
          )

          SkillsCard(
            skills = resume.skills,
            onChange = { resume = resume.copy(skills = it) }
          )

          Spacer(modifier = Modifier.height(32.dp))
        }
      } else {
        // Live Preview Mode
        Column(
          modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
        ) {
          ResumeLivePreview(resume = resume)
          Spacer(modifier = Modifier.height(32.dp))
        }
      }
    }
  }

  // Gallery Modal
  if (showTemplateGallery) {
    TemplateGalleryScreen(
      currentTemplateId = resume.templateId,
      onSelectTemplate = { newId ->
        resume = resume.copy(templateId = newId)
        showTemplateGallery = false
      },
      onClose = { showTemplateGallery = false }
    )
  }

  // ATS Modal
  if (showAtsAudit) {
    AtsReadinessSheet(
      resume = resume,
      onDismiss = { showAtsAudit = false }
    )
  }

  // Job Matcher Modal
  if (showJobMatcher) {
    JobMatcherSheet(
      resume = resume,
      onDismiss = { showJobMatcher = false }
    )
  }

  // Live Preview Modal
  if (showLivePreviewDialog) {
    LivePreviewDialog(resume = resume, onDismiss = { showLivePreviewDialog = false })
  }

  // Content Enhancer Modal
  enhancerInitialText?.let { text ->
    ContentEnhancerSheet(
      initialText = text,
      onApply = { enhanced ->
        enhancerApplyCallback?.invoke(enhanced)
      },
      onDismiss = {
        enhancerInitialText = null
        enhancerApplyCallback = null
      }
    )
  }
}
