package rs.edu.raf.showtime.auth.util

private val usernameRegex = Regex("^[A-Za-z0-9_]+$")

fun validateFullName(fullName: String): String? {
    return when {
        fullName.isBlank() -> "Full name is required"
        else -> null
    }
}

fun validateUsername(username: String): String? {
    return when {
        username.isBlank() -> "Username is required"
        username.length < 3 -> "Username must be at least 3 characters"
        !usernameRegex.matches(username) -> "Use only letters, digits, and underscores"
        else -> null
    }
}

fun validatePassword(password: String): String? {
    return when {
        password.isBlank() -> "Password is required"
        password.length < 8 -> "Password must be at least 8 characters"
        else -> null
    }
}
