with open('app/src/main/java/com/example/ui/studio/CareereStudioScreen.kt', 'r') as f:
    code = f.read()

imports = """
import com.example.data.DatabaseProvider
import com.example.data.ResumeRepository
import com.example.data.toEntity
import kotlinx.coroutines.delay
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
"""
code = code.replace("import com.example.ui.tools.*", "import com.example.ui.tools.*\n" + imports)

state_vars_orig = """  var showProfileMenu by remember { mutableStateOf(false) }

  // Enhancer Sheet State
  var enhancerInitialText by remember { mutableStateOf<String?>(null) }
  var enhancerApplyCallback by remember { mutableStateOf<((String) -> Unit)?>(null) }"""

state_vars_new = """  var showProfileMenu by remember { mutableStateOf(false) }
  var showLivePreviewDialog by remember { mutableStateOf(false) }

  // Enhancer Sheet State
  var enhancerInitialText by remember { mutableStateOf<String?>(null) }
  var enhancerApplyCallback by remember { mutableStateOf<((String) -> Unit)?>(null) }

  // Auto-Save Mechanism
  val db = remember { DatabaseProvider.getDatabase(context) }
  val repository = remember { ResumeRepository(db.resumeDao()) }
  
  LaunchedEffect(resume) {
      delay(1000)
      repository.insert(resume.toEntity())
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
  }"""
code = code.replace(state_vars_orig, state_vars_new)


top_app_bar_orig = """        title = {
          Row(verticalAlignment = Alignment.CenterVertically) {"""

top_app_bar_new = """        title = {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.padding(end = 12.dp)) {
                LinearProgressIndicator(
                    progress = { completionProgress },
                    modifier = Modifier.width(60.dp).height(4.dp),
                    color = BurgundyPrimary,
                    trackColor = WarmStoneLight
                )
            }"""
code = code.replace(top_app_bar_orig, top_app_bar_new)


bottom_bar_buttons_orig = """            OutlinedButton(
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
          }"""

bottom_bar_buttons_new = """            OutlinedButton(
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
          }"""
code = code.replace(bottom_bar_buttons_orig, bottom_bar_buttons_new)

dialogs_orig = """  // Content Enhancer Modal
  enhancerInitialText?.let { text ->"""
dialogs_new = """  // Live Preview Modal
  if (showLivePreviewDialog) {
    LivePreviewDialog(resume = resume, onDismiss = { showLivePreviewDialog = false })
  }

  // Content Enhancer Modal
  enhancerInitialText?.let { text ->"""
code = code.replace(dialogs_orig, dialogs_new)

with open('app/src/main/java/com/example/ui/studio/CareereStudioScreen.kt', 'w') as f:
    f.write(code)
