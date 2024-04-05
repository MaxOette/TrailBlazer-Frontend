package de.max.trailblazerfrontendv1.Api

data class HealthResponse(
    val health: Boolean
)

data class RegisterUserData(
    val email: String,
    val password: String,
    val firstname: String,
    val lastname: String
)
