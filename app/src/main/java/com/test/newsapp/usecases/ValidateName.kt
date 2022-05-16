package com.test.newsapp.usecases

import android.util.Patterns

class ValidateName {
    fun execute(name: String): ValidationResult {
        if(name.isBlank()) {
            return ValidationResult(
                successful = false,
                errorMessage = "Name cannot be empty"
            )
        }

        return ValidationResult(
            successful = true
        )
    }
}