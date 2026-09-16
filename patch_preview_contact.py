import re

with open('app/src/main/java/com/example/ui/preview/ResumeLivePreview.kt', 'r') as f:
    code = f.read()

if "import com.example.util.InputValidator" not in code:
    code = code.replace("import com.example.model.ResumeSettings", "import com.example.model.ResumeSettings\nimport com.example.util.InputValidator")

old_contact = """        val contacts = mutableListOf<String>()
        if (resume.personalInfo.email.isNotBlank()) contacts.add(resume.personalInfo.email)
        if (resume.personalInfo.phone.isNotBlank()) contacts.add(resume.personalInfo.phone)"""

new_contact = """        val contacts = mutableListOf<String>()
        if (resume.personalInfo.email.isNotBlank() && InputValidator.isValidEmail(resume.personalInfo.email)) contacts.add(resume.personalInfo.email)
        if (resume.personalInfo.phone.isNotBlank() && InputValidator.isValidPhone(resume.personalInfo.phone)) contacts.add(resume.personalInfo.phone)"""

code = code.replace(old_contact, new_contact)

with open('app/src/main/java/com/example/ui/preview/ResumeLivePreview.kt', 'w') as f:
    f.write(code)
