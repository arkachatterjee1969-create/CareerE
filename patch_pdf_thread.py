with open('app/src/main/java/com/example/ui/studio/CareereStudioScreen.kt', 'r') as f:
    code = f.read()

old_pdf_call = """          Button(
            onClick = {
              try {
                val pdfFile = PdfExportManager.generatePdf(context, resume)
                PdfExportManager.sharePdf(context, pdfFile)
                Toast.makeText(context, "CV exported: ${pdfFile.name}", Toast.LENGTH_SHORT).show()
              } catch (e: Exception) {
                Toast.makeText(context, "Export error: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
              }
            },"""

new_pdf_call = """          val coroutineScope = rememberCoroutineScope()
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
            },"""

code = code.replace(old_pdf_call, new_pdf_call)

with open('app/src/main/java/com/example/ui/studio/CareereStudioScreen.kt', 'w') as f:
    f.write(code)
