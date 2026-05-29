package rs.edu.raf.showtime.movies.ui.screen.moviedetails.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import kotlin.math.round

@Composable
fun SectionTitle(title: String) {
    Text(text = title,
        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Black),
        modifier = Modifier.padding(bottom = 4.dp))
}

@Composable
fun RatingChip(label: String, rating: Float, votes: Int?){
    val value = round(rating * 10f) / 10f
    Column(modifier = Modifier.padding(vertical = 12.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(4.dp)){
        Text(text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSecondaryContainer)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = " $value",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )
            if (votes != null) {
                Text(
                    text = formatVotes(votes),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.75f)
                )
            }
        }
    }
}

@Composable
fun CollectionCard(collection: rs.edu.raf.showtime.movies.domain.Collection) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer)
    ) {
        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            if (collection.posterPath != null) {
                AsyncImage(
                    model = "https://image.tmdb.org/t/p/w185${collection.posterPath}",
                    contentDescription = collection.name,
                    modifier = Modifier.size(60.dp, 90.dp).background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(4.dp)),
                    contentScale = ContentScale.Crop
                )
            }
            Spacer(Modifier.width(16.dp))
            Text(text = collection.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        }
    }
}

fun formatVotes(votes: Int): String = when {
    votes >= 1_000_000 -> "${round(votes / 100_000.0) / 10.0}M"
    votes >= 1_000 -> "${round(votes / 100.0) / 10.0}K"
    else -> votes.toString()
}

fun formatRuntime(minutes: Int): String {
    val h = minutes / 60
    val m = minutes % 60
    return if (h > 0) "${h}h ${m}m" else "${m}min"
}

fun formatMoney(amount: Long): String = when {
    amount >= 1_000_000_000 -> {
        val v = amount / 1_000_000_000.0
        "$${round(v * 10) / 10.0}B"
    }
    amount >= 1_000_000 -> {
        val v = amount / 1_000_000.0
        "$${round(v * 10) / 10.0}M"
    }
    else -> "$$amount"
}