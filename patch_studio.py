with open('app/src/main/java/com/example/ui/studio/CareereStudioScreen.kt', 'r') as f:
    code = f.read()

import_lines = "import com.example.ui.editor.*"
new_import = "import com.example.ui.editor.*\nimport com.example.ui.editor.ThemeCustomizationCard"
if "ThemeCustomizationCard" not in code:
    code = code.replace(import_lines, new_import)

old_cards = """          PersonalInfoCard(
            personalInfo = resume.personalInfo,
            onChange = { resume = resume.copy(personalInfo = it) }
          )"""

new_cards = """          ThemeCustomizationCard(
            settings = resume.settings,
            onChange = { resume = resume.copy(settings = it) }
          )
          PersonalInfoCard(
            personalInfo = resume.personalInfo,
            onChange = { resume = resume.copy(personalInfo = it) }
          )"""

code = code.replace(old_cards, new_cards)

with open('app/src/main/java/com/example/ui/studio/CareereStudioScreen.kt', 'w') as f:
    f.write(code)
