package com.example.export

import android.content.Context
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import android.net.Uri
import androidx.core.content.FileProvider
import com.example.model.PageSize
import com.example.model.ResumeData
import com.example.util.InputValidator
import com.example.template.TemplateDefinition
import com.example.template.TemplateRepository
import java.io.File
import java.io.FileOutputStream

object PdfExportManager {

  /**
   * Generates a pristine, ATS-friendly vector PDF document from ResumeData.
   * Returns the generated file.
   */
  fun generatePdf(context: Context, resume: ResumeData): File {
    val template = TemplateRepository.getById(resume.templateId)
    val document = PdfDocument()

    val width = resume.settings.pageSize.widthPt
    val height = resume.settings.pageSize.heightPt

    // Margins
    val marginX = 40f
    val marginY = 40f
    val contentWidth = width - marginX * 2

    val pageInfo = PdfDocument.PageInfo.Builder(width, height, 1).create()
    var currentPage = document.startPage(pageInfo)
    var canvas = currentPage.canvas

    // Paints
    val accentColor = try {
      android.graphics.Color.parseColor(resume.settings.primaryColorHex)
    } catch (e: Exception) {
      android.graphics.Color.parseColor("#72232B")
    }

    val nearBlack = android.graphics.Color.parseColor("#1F1F1F")
    val mutedGray = android.graphics.Color.parseColor("#5E5752")
    val hairlineColor = android.graphics.Color.parseColor("#D8D1C7")

    val titlePaint = Paint().apply {
      isAntiAlias = true
      color = accentColor
      textSize = 22f
      typeface = Typeface.create(Typeface.SERIF, Typeface.BOLD)
    }

    val subtitlePaint = Paint().apply {
      isAntiAlias = true
      color = mutedGray
      textSize = 12f
      typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL)
    }

    val contactPaint = Paint().apply {
      isAntiAlias = true
      color = nearBlack
      textSize = 9.5f
      typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL)
    }

    val sectionHeaderPaint = Paint().apply {
      isAntiAlias = true
      color = accentColor
      textSize = 12f
      typeface = Typeface.create(Typeface.SERIF, Typeface.BOLD)
    }

    val itemTitlePaint = Paint().apply {
      isAntiAlias = true
      color = nearBlack
      textSize = 10.5f
      typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD)
    }

    val itemSubPaint = Paint().apply {
      isAntiAlias = true
      color = mutedGray
      textSize = 9.5f
      typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.ITALIC)
    }

    val bodyPaint = Paint().apply {
      isAntiAlias = true
      color = nearBlack
      textSize = 9.5f
      typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL)
    }

    val linePaint = Paint().apply {
      color = hairlineColor
      strokeWidth = 0.8f
    }

    var currentY = marginY + 20f
    var pageNumber = 1

    fun checkPageBreak(requiredHeight: Float) {
      if (currentY + requiredHeight > height - marginY) {
        document.finishPage(currentPage)
        pageNumber++
        val nextPageInfo = PdfDocument.PageInfo.Builder(width, height, pageNumber).create()
        currentPage = document.startPage(nextPageInfo)
        canvas = currentPage.canvas
        currentY = marginY + 20f
      }
    }

    // 1. Header (Name & Headline)
    val name = resume.personalInfo.fullName.ifBlank { "Candidate Name" }
    canvas.drawText(name, marginX, currentY, titlePaint)
    currentY += 18f

    if (resume.personalInfo.professionalTitle.isNotBlank()) {
      canvas.drawText(resume.personalInfo.professionalTitle, marginX, currentY, subtitlePaint)
      currentY += 16f
    }

    // Contact Details Line
    val contacts = mutableListOf<String>()
    if (resume.personalInfo.email.isNotBlank() && InputValidator.isValidEmail(resume.personalInfo.email)) contacts.add(resume.personalInfo.email)
    if (resume.personalInfo.phone.isNotBlank() && InputValidator.isValidPhone(resume.personalInfo.phone)) contacts.add(resume.personalInfo.phone)
    val location = listOf(resume.personalInfo.city, resume.personalInfo.country).filter { it.isNotBlank() }.joinToString(", ")
    if (location.isNotBlank()) contacts.add(location)
    if (resume.personalInfo.linkedinUrl.isNotBlank()) contacts.add(resume.personalInfo.linkedinUrl)

    if (contacts.isNotEmpty()) {
      val contactLine = contacts.joinToString("  •  ")
      canvas.drawText(contactLine, marginX, currentY, contactPaint)
      currentY += 14f
    }

    // Decorative top divider
    canvas.drawLine(marginX, currentY, marginX + contentWidth, currentY, linePaint)
    currentY += 18f

    // 2. Summary
    if (resume.personalInfo.summary.isNotBlank()) {
      checkPageBreak(50f)
      val sectionLabel = if (resume.personalInfo.isObjective) "CAREER OBJECTIVE" else "PROFESSIONAL SUMMARY"
      drawSectionHeading(canvas, marginX, contentWidth, currentY, sectionLabel, sectionHeaderPaint, linePaint)
      currentY += 16f

      val wrappedSummary = wrapText(resume.personalInfo.summary, contentWidth, bodyPaint)
      for (line in wrappedSummary) {
        checkPageBreak(14f)
        canvas.drawText(line, marginX, currentY, bodyPaint)
        currentY += 13f
      }
      currentY += 12f
    }

    // 3. Experience
    if (resume.experiences.isNotEmpty()) {
      checkPageBreak(50f)
      drawSectionHeading(canvas, marginX, contentWidth, currentY, "WORK EXPERIENCE", sectionHeaderPaint, linePaint)
      currentY += 16f

      for (exp in resume.experiences) {
        checkPageBreak(40f)
        val expTitle = exp.jobTitle.ifBlank { "Role" }
        val expCompany = exp.company.ifBlank { "Company" }
        canvas.drawText("$expTitle  —  $expCompany", marginX, currentY, itemTitlePaint)

        val dateStr = "${exp.startDate} – ${if (exp.isCurrent) "Present" else exp.endDate}".trim()
        val locDate = listOf(exp.location, dateStr).filter { it.isNotBlank() }.joinToString(" | ")
        val dateWidth = itemSubPaint.measureText(locDate)
        canvas.drawText(locDate, marginX + contentWidth - dateWidth, currentY, itemSubPaint)
        currentY += 14f

        for (bullet in exp.bullets) {
          if (bullet.isNotBlank()) {
            val wrappedBullets = wrapText("•  $bullet", contentWidth - 12f, bodyPaint)
            for (line in wrappedBullets) {
              checkPageBreak(14f)
              canvas.drawText(line, marginX + 8f, currentY, bodyPaint)
              currentY += 13f
            }
          }
        }
        currentY += 8f
      }
      currentY += 6f
    }

    // 4. Education
    if (resume.educations.isNotEmpty()) {
      checkPageBreak(50f)
      drawSectionHeading(canvas, marginX, contentWidth, currentY, "EDUCATION", sectionHeaderPaint, linePaint)
      currentY += 16f

      for (edu in resume.educations) {
        checkPageBreak(36f)
        val degreeStr = listOf(edu.degree, edu.fieldOfStudy).filter { it.isNotBlank() }.joinToString(", ")
        canvas.drawText(degreeStr.ifBlank { "Degree" }, marginX, currentY, itemTitlePaint)

        val dateStr = "${edu.startDate} – ${if (edu.isCurrent) "Present" else edu.endDate}".trim()
        val dateWidth = itemSubPaint.measureText(dateStr)
        canvas.drawText(dateStr, marginX + contentWidth - dateWidth, currentY, itemSubPaint)
        currentY += 14f

        val instLoc = listOf(edu.institution, edu.location, edu.gpa).filter { it.isNotBlank() }.joinToString(" • ")
        canvas.drawText(instLoc, marginX, currentY, itemSubPaint)
        currentY += 16f
      }
    }

    // 5. Skills
    if (resume.skills.isNotEmpty()) {
      checkPageBreak(40f)
      drawSectionHeading(canvas, marginX, contentWidth, currentY, "CORE SKILLS & EXPERTISE", sectionHeaderPaint, linePaint)
      currentY += 16f

      val skillsText = resume.skills.joinToString("  •  ") { it.name }
      val wrappedSkills = wrapText(skillsText, contentWidth, bodyPaint)
      for (line in wrappedSkills) {
        checkPageBreak(14f)
        canvas.drawText(line, marginX, currentY, bodyPaint)
        currentY += 14f
      }
      currentY += 12f
    }

    // 6. Projects
    if (resume.projects.isNotEmpty()) {
      checkPageBreak(40f)
      drawSectionHeading(canvas, marginX, contentWidth, currentY, "SELECTED PROJECTS", sectionHeaderPaint, linePaint)
      currentY += 16f

      for (proj in resume.projects) {
        checkPageBreak(30f)
        canvas.drawText("${proj.name}  (${proj.role})", marginX, currentY, itemTitlePaint)
        if (proj.date.isNotBlank()) {
          val dw = itemSubPaint.measureText(proj.date)
          canvas.drawText(proj.date, marginX + contentWidth - dw, currentY, itemSubPaint)
        }
        currentY += 14f

        if (proj.description.isNotBlank()) {
          val wrappedDesc = wrapText(proj.description, contentWidth, bodyPaint)
          for (line in wrappedDesc) {
            checkPageBreak(14f)
            canvas.drawText(line, marginX, currentY, bodyPaint)
            currentY += 13f
          }
        }
        currentY += 8f
      }
    }

    // 7. Languages
    if (resume.languages.isNotEmpty()) {
      checkPageBreak(30f)
      drawSectionHeading(canvas, marginX, contentWidth, currentY, "LANGUAGES", sectionHeaderPaint, linePaint)
      currentY += 16f

      val langStr = resume.languages.joinToString("    ") { "${it.language}: ${it.proficiency}" }
      canvas.drawText(langStr, marginX, currentY, bodyPaint)
      currentY += 16f
    }

    // Finish last page
    document.finishPage(currentPage)

    // Save to cache
    val cleanName = resume.personalInfo.fullName.replace(" ", "_").filter { it.isLetterOrDigit() || it == '_' }
    val filename = if (cleanName.isNotBlank()) "${cleanName}_Careere_CV.pdf" else "Careere_CV.pdf"

    val exportDir = File(context.cacheDir, "exports").apply { mkdirs() }
    val outputFile = File(exportDir, filename)

    val outputStream = FileOutputStream(outputFile)
    document.writeTo(outputStream)
    outputStream.close()
    document.close()

    return outputFile
  }

  private fun drawSectionHeading(
    canvas: Canvas,
    x: Float,
    width: Float,
    y: Float,
    title: String,
    paint: Paint,
    linePaint: Paint
  ) {
    canvas.drawText(title, x, y, paint)
    val textWidth = paint.measureText(title)
    canvas.drawLine(x + textWidth + 12f, y - 4f, x + width, y - 4f, linePaint)
  }

  private fun wrapText(text: String, maxWidth: Float, paint: Paint): List<String> {
    val words = text.split(" ")
    val lines = mutableListOf<String>()
    var currentLine = StringBuilder()

    for (word in words) {
      val testLine = if (currentLine.isEmpty()) word else "$currentLine $word"
      val measure = paint.measureText(testLine)
      if (measure <= maxWidth) {
        currentLine = StringBuilder(testLine)
      } else {
        if (currentLine.isNotEmpty()) {
          lines.add(currentLine.toString())
        }
        currentLine = StringBuilder(word)
      }
    }
    if (currentLine.isNotEmpty()) {
      lines.add(currentLine.toString())
    }
    return lines
  }

  /**
   * Shares the generated PDF file using system share sheet
   */
  fun sharePdf(context: Context, file: File) {
    try {
      val uri: Uri = FileProvider.getUriForFile(
        context,
        "${context.packageName}.fileprovider",
        file
      )
      val intent = Intent(Intent.ACTION_SEND).apply {
        type = "application/pdf"
        putExtra(Intent.EXTRA_STREAM, uri)
        putExtra(Intent.EXTRA_SUBJECT, file.name)
        flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
      }
      context.startActivity(Intent.createChooser(intent, "Share or Save CV"))
    } catch (e: Exception) {
      // Fallback intent
      val intent = Intent(Intent.ACTION_SEND).apply {
        type = "application/pdf"
        putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file))
        flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
      }
      context.startActivity(Intent.createChooser(intent, "Share CV"))
    }
  }
}
