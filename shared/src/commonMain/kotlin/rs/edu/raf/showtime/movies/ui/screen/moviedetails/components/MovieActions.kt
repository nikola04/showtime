package rs.edu.raf.showtime.movies.ui.screen.moviedetails.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun MovieActions(
    isFavorite: Boolean,
    isWatchlist: Boolean,
    onFavoriteClick: () -> Unit,
    onWatchlistClick: () -> Unit,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        FilledTonalButton(
            onClick = onFavoriteClick
        ) {
            Icon(
                imageVector = if (isFavorite) {
                    Icons.Default.Favorite
                } else {
                    Icons.Default.FavoriteBorder
                },
                contentDescription = null
            )

            Spacer(Modifier.width(8.dp))

            Text(
                if (isFavorite) {
                    "Favorited"
                } else {
                    "Favorite"
                }
            )
        }

        FilledTonalButton(
            onClick = onWatchlistClick
        ) {
            Icon(
                imageVector = if (isWatchlist) {
                    Icons.Default.Bookmark
                } else {
                    Icons.Default.BookmarkBorder
                },
                contentDescription = null
            )

            Spacer(Modifier.width(8.dp))

            Text(
                if (isWatchlist) {
                    "In Watchlist"
                } else {
                    "Watchlist"
                }
            )
        }
    }
}