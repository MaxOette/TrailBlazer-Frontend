package de.max.trailblazerfrontendv1.Api

data class HealthResponse(
    val health: Boolean
)

data class RegisterUserData(
    val email: String,
    val password: String,
    val username: String
)

data class LoginUserData(
    val email: String,
    val password: String
)

data class ActiveUserData(
    val token: String,
    val type: String,
    val username: String,
    val email: String,
    val id: Int,
    val refresh_token: String
)

data class TileData(
    val zoomLevel: Int,
    val opacity: Int,
    val posUpperLeft: DoubleArray,
    val posUpperRight: DoubleArray,
    val posLowerRight: DoubleArray,
    val posLowerLeft: DoubleArray,
    val xtile: Int,
    val ytile: Int
)

data class Friend(
    val uuid: String,
    val email: String,
    val acceptedAt: String,
    val stats: Float
)

data class Invite(
    val uuid:String,
    val email: String,
    val sendAt: String
)

data class CountyStats(
    var kuerzel: String,
    val percentage: Float,
    var imageResource: Int
)

data class Achievement(
    val title: String,
    val description: String,
    val achieved: Boolean,
    var imageResource: Int
)

