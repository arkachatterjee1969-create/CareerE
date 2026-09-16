import re

with open('app/src/main/java/com/example/ui/studio/CareereStudioScreen.kt', 'r') as f:
    code = f.read()

code = code.replace("import com.example.data.toEntity", "import com.example.data.toEntity\nimport com.example.data.toResumeData")

with open('app/src/main/java/com/example/ui/studio/CareereStudioScreen.kt', 'w') as f:
    f.write(code)
