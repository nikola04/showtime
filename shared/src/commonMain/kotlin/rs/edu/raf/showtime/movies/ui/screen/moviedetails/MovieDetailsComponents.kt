package rs.edu.raf.showtime.movies.ui.screen.moviedetails

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import rs.edu.raf.showtime.movies.data.model.Collection
import kotlin.math.round

@Composable
fun SectionTitle(title: String) {
    Text(text = title,
        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Black),
        modifier = Modifier.padding(bottom = 4.dp))
}

@Composable
fun CollectionCard(collection: Collection){
    Card(modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp)) {
        Row(modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            if (collection.posterPath != null) {
                AsyncImage(model = "https://image.tmdb.org/t/p/w154${collection.posterPath}",
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.size(60.dp, 90.dp).clip(RoundedCornerShape(8.dp))
                )
            }
            Text(text = collection.name,
                style = MaterialTheme.typography.titleSmall)
        }
    }
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
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
        }
    }
}

private fun formatVotes(votes: Int): String = when {
    votes >= 1_000_000 -> {
        val f = votes / 1_000_000f
        "${round(f * 10f) / 10f}M"
    }
    votes >= 1_000 -> {
        val f = votes / 1_000f
        "${round(f * 10f) / 10f}K"
    }
    else -> votes.toString()
}