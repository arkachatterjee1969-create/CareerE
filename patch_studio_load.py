import re

with open('app/src/main/java/com/example/ui/studio/CareereStudioScreen.kt', 'r') as f:
    code = f.read()

# Add kotlinx.coroutines.flow.firstOrNull import
if "import kotlinx.coroutines.flow.firstOrNull" not in code:
    code = code.replace("import kotlinx.coroutines.delay", "import kotlinx.coroutines.delay\nimport kotlinx.coroutines.flow.firstOrNull\nimport kotlinx.coroutines.launch")

old_init = """  // Auto-Save Mechanism
  val db = remember { DatabaseProvider.getDatabase(context) }
  val repository = remember { ResumeRepository(db.resumeDao()) }
  
  LaunchedEffect(resume) {
      delay(1000)
      repository.insert(resume.toEntity())
  }"""

new_init = """  // Auto-Save Mechanism
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
  }"""

code = code.replace(old_init, new_init)

with open('app/src/main/java/com/example/ui/studio/CareereStudioScreen.kt', 'w') as f:
    f.write(code)
