package rs.edu.raf.showtime.core.util

fun formatVotes(votes: Int?): String {
    if (votes == null) return ""
    return when {
        votes >= 1_000_000 -> {
            val millions = votes / 1_000_000.0
            val rounded = (millions * 10).toInt() / 10.0
            "${rounded}M votes"
        }
        votes >= 1_000 -> "${votes / 1000}K votes"
        else -> "$votes votes"
    }
}
