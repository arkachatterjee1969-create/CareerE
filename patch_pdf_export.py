import re

with open('app/src/main/java/com/example/export/PdfExportManager.kt', 'r') as f:
    code = f.read()

# Make sure InputValidator is imported
if "import com.example.util.InputValidator" not in code:
    code = code.replace("import com.example.model.ResumeData", "import com.example.model.ResumeData\nimport com.example.util.InputValidator")

old_contact = """    // Contact Details Line
    val contacts = mutableListOf<String>()
    if (resume.personalInfo.email.isNotBlank()) contacts.add(resume.personalInfo.email)
    if (resume.personalInfo.phone.isNotBlank()) contacts.add(resume.personalInfo.phone)"""

new_contact = """    // Contact Details Line
    val contacts = mutableListOf<String>()
    if (resume.personalInfo.email.isNotBlank() && InputValidator.isValidEmail(resume.personalInfo.email)) contacts.add(resume.personalInfo.email)
    if (resume.personalInfo.phone.isNotBlank() && InputValidator.isValidPhone(resume.personalInfo.phone)) contacts.add(resume.personalInfo.phone)"""

code = code.replace(old_contact, new_contact)

with open('app/src/main/java/com/example/export/PdfExportManager.kt', 'w') as f:
    f.write(code)
