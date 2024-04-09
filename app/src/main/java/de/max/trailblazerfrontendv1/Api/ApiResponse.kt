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

data class LoginUserData(
    val email: String,
    val password: String
)

data class ActiveUserData(
    val token: String,
    val type: String,
    val email: String,
    val id: Int,
    val refresh_token: String
)