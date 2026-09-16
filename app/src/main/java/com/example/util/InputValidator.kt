package com.example.util

import android.util.Patterns

object InputValidator {
  
  /**
   * Validates if the given email is in a correct format.
   */
  fun isValidEmail(email: String): Boolean {
    if (email.isBlank()) return false
    return Patterns.EMAIL_ADDRESS.matcher(email).matches()
  }

  /**
   * Validates if the given phone number is in a correct format.
   */
  fun isValidPhone(phone: String): Boolean {
    if (phone.isBlank()) return false
    return Patterns.PHONE.matcher(phone).matches()
  }

  /**
   * Validates a date string in MM/YYYY or YYYY format.
   */
  fun isValidDate(date: String): Boolean {
    if (date.isBlank()) return false
    // Basic regex for MM/YYYY or just YYYY
    val regex = Regex("""^(0[1-9]|1[0-2])/\d{4}$|^\d{4}$""")
    return regex.matches(date)
  }
}
