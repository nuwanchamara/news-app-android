package com.test.newsapp.usecases

data class ValidationResult(
    val successful: Boolean,
    val errorMessage: String? = null
)