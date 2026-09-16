with open('app/src/main/java/com/example/ui/preview/ResumeLivePreview.kt', 'r') as f:
    code = f.read()

import_lines = "import com.example.model.ResumeData"
new_import = "import com.example.model.ResumeData\nimport com.example.model.ResumeSettings"
if "import com.example.model.ResumeSettings" not in code:
    code = code.replace(import_lines, new_import)

old_colors = """  val primaryAccent = try {
    Color(android.graphics.Color.parseColor(template.primaryAccentHex))
  } catch (e: Exception) {
    BurgundyPrimary
  }

  val secondaryAccent = try {
    Color(android.graphics.Color.parseColor(template.secondaryColorHex))
  } catch (e: Exception) {
    MutedRose
  }

  val headingFont = when (template.typography) {
    TypographyStyle.SERIF_EDITORIAL -> FontFamily.Serif
    TypographyStyle.MONO_TECHNICAL -> FontFamily.Monospace
    else -> FontFamily.SansSerif
  }

  val bodyFont = when (template.typography) {
    TypographyStyle.MONO_TECHNICAL -> FontFamily.Monospace
    else -> FontFamily.SansSerif
  }"""

new_colors = """  val primaryAccent = try {
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
  }"""

code = code.replace(old_colors, new_colors)

with open('app/src/main/java/com/example/ui/preview/ResumeLivePreview.kt', 'w') as f:
    f.write(code)
