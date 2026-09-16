with open('app/src/main/java/com/example/export/PdfExportManager.kt', 'r') as f:
    code = f.read()

old_colors = """    // Paints
    val accentColor = try {
      android.graphics.Color.parseColor(template.primaryAccentHex)
    } catch (e: Exception) {
      android.graphics.Color.parseColor("#72232B")
    }"""

new_colors = """    // Paints
    val accentColor = try {
      android.graphics.Color.parseColor(resume.settings.primaryColorHex)
    } catch (e: Exception) {
      android.graphics.Color.parseColor("#72232B")
    }"""

code = code.replace(old_colors, new_colors)

old_fonts = """    val titlePaint = Paint().apply {
      color = accentColor
      textSize = 24f
      isAntiAlias = true
      typeface = Typeface.create(Typeface.SERIF, Typeface.BOLD)
    }

    val headingPaint = Paint().apply {
      color = accentColor
      textSize = 14f
      isAntiAlias = true
      typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD)
    }

    val bodyPaint = Paint().apply {
      color = android.graphics.Color.DKGRAY
      textSize = 11f
      isAntiAlias = true
      typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL)
    }"""

new_fonts = """    val isSerif = resume.settings.fontChoice == "Playfair Display / Serif"
    val isMono = resume.settings.fontChoice == "Fira Code / Mono"
    val headingTypeface = if (isSerif) Typeface.SERIF else if (isMono) Typeface.MONOSPACE else Typeface.SANS_SERIF
    val bodyTypeface = if (isMono) Typeface.MONOSPACE else Typeface.SANS_SERIF

    val titlePaint = Paint().apply {
      color = accentColor
      textSize = 24f
      isAntiAlias = true
      typeface = Typeface.create(headingTypeface, Typeface.BOLD)
    }

    val headingPaint = Paint().apply {
      color = accentColor
      textSize = 14f
      isAntiAlias = true
      typeface = Typeface.create(headingTypeface, Typeface.BOLD)
    }

    val bodyPaint = Paint().apply {
      color = android.graphics.Color.DKGRAY
      textSize = 11f
      isAntiAlias = true
      typeface = Typeface.create(bodyTypeface, Typeface.NORMAL)
    }"""

code = code.replace(old_fonts, new_fonts)

with open('app/src/main/java/com/example/export/PdfExportManager.kt', 'w') as f:
    f.write(code)
