import re

with open('app/src/main/java/com/example/ui/editor/SectionForms.kt', 'r') as f:
    code = f.read()

if "import com.example.util.InputValidator" not in code:
    code = code.replace("import com.example.model.*", "import com.example.model.*\nimport com.example.util.InputValidator")

old_editorial = """fun EditorialTextField(
  value: String,
  onValueChange: (String) -> Unit,
  label: String,
  placeholder: String,
  modifier: Modifier = Modifier
) {
  OutlinedTextField(
    value = value,
    onValueChange = onValueChange,
    label = { Text(label, fontSize = 11.5.sp) },
    placeholder = { Text(placeholder, fontSize = 12.sp) },
    modifier = modifier.fillMaxWidth(),
    colors = OutlinedTextFieldDefaults.colors(
      focusedContainerColor = WarmIvorySurface,
      unfocusedContainerColor = WarmIvorySurface,
      focusedBorderColor = BurgundyPrimary,
      unfocusedBorderColor = WarmStoneBorder
    ),
    shape = RoundedCornerShape(8.dp),
    singleLine = true
  )
}"""

new_editorial = """fun EditorialTextField(
  value: String,
  onValueChange: (String) -> Unit,
  label: String,
  placeholder: String,
  modifier: Modifier = Modifier,
  isError: Boolean = false,
  errorMessage: String? = null
) {
  OutlinedTextField(
    value = value,
    onValueChange = onValueChange,
    label = { Text(label, fontSize = 11.5.sp) },
    placeholder = { Text(placeholder, fontSize = 12.sp) },
    modifier = modifier.fillMaxWidth(),
    isError = isError,
    supportingText = {
      if (isError && errorMessage != null) {
        Text(errorMessage, color = MaterialTheme.colorScheme.error)
      }
    },
    colors = OutlinedTextFieldDefaults.colors(
      focusedContainerColor = WarmIvorySurface,
      unfocusedContainerColor = WarmIvorySurface,
      focusedBorderColor = BurgundyPrimary,
      unfocusedBorderColor = WarmStoneBorder,
      errorBorderColor = MaterialTheme.colorScheme.error,
      errorLabelColor = MaterialTheme.colorScheme.error
    ),
    shape = RoundedCornerShape(8.dp),
    singleLine = true
  )
}"""

code = code.replace(old_editorial, new_editorial)

old_email_phone = """      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        EditorialTextField(
          value = personalInfo.email,
          onValueChange = { onChange(personalInfo.copy(email = it)) },
          label = "Email Address *",
          placeholder = "alexandre@example.com",
          modifier = Modifier.weight(1f)
        )
        EditorialTextField(
          value = personalInfo.phone,
          onValueChange = { onChange(personalInfo.copy(phone = it)) },
          label = "Phone Number",
          placeholder = "+33 6 12 34 56 78",
          modifier = Modifier.weight(1f)
        )
      }"""

new_email_phone = """      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        val isEmailError = personalInfo.email.isNotBlank() && !InputValidator.isValidEmail(personalInfo.email)
        val isPhoneError = personalInfo.phone.isNotBlank() && !InputValidator.isValidPhone(personalInfo.phone)

        EditorialTextField(
          value = personalInfo.email,
          onValueChange = { onChange(personalInfo.copy(email = it)) },
          label = "Email Address *",
          placeholder = "alexandre@example.com",
          modifier = Modifier.weight(1f),
          isError = isEmailError,
          errorMessage = "Invalid email format"
        )
        EditorialTextField(
          value = personalInfo.phone,
          onValueChange = { onChange(personalInfo.copy(phone = it)) },
          label = "Phone Number",
          placeholder = "+33 6 12 34 56 78",
          modifier = Modifier.weight(1f),
          isError = isPhoneError,
          errorMessage = "Invalid phone format"
        )
      }"""

code = code.replace(old_email_phone, new_email_phone)

with open('app/src/main/java/com/example/ui/editor/SectionForms.kt', 'w') as f:
    f.write(code)
