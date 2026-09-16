with open('app/src/main/java/com/example/data/AppDatabase.kt', 'r') as f:
    code = f.read()

code = code.replace("Converters::class", "ResumeConverters::class")

with open('app/src/main/java/com/example/data/AppDatabase.kt', 'w') as f:
    f.write(code)
