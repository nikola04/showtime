package rs.edu.raf.showtime.movies.ui.screen.moviedetails

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import org.koin.compose.viewmodel.koinViewModel
import rs.edu.raf.showtime.movies.data.model.CastMemberDTO
import rs.edu.raf.showtime.movies.data.model.MovieDTO
import kotlin.math.round

@Composable
fun MovieDetailsScreen(
    onBackClick: () -> Unit
) {
    val viewModel: MovieDetailsViewModel = koinViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()
    val uriHandler = androidx.compose.ui.platform.LocalUriHandler.current

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is MovieDetailsContract.Effect.NavigateBack -> {
                    onBackClick()
                }
                is MovieDetailsContract.Effect.OpenYoutube -> {
                    uriHandler.openUri("https://www.youtube.com/watch?v=${effect.id}")
                }
            }
        }
    }

    Scaffold{
        Box(modifier = Modifier.fillMaxSize()) {
            when (val screenState = state.screenState) {
                is MovieDetailsContract.ScreenState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is MovieDetailsContract.ScreenState.Error -> {
                    Column(modifier = Modifier.align(Alignment.Center), horizontalAlignment = Alignment.CenterHorizontally){
                        Text("Error loading movie details", style = MaterialTheme.typography.bodyLarge)
                        Spacer(Modifier.height(8.dp))
                        Text(screenState.message, style = MaterialTheme.typography.bodySmall)
                        Spacer(Modifier.height(8.dp))
                        Button(onClick = { viewModel.onEvent(event = MovieDetailsContract.Event.RetryClicked)}) {
                            Text("Retry")
                        }
                    }
                }
                is MovieDetailsContract.ScreenState.Success -> {
                    val movie = screenState.movie
                    val images = screenState.images
                    val trailer = screenState.trailer
                    val scrollState = rememberScrollState()
                    Column(modifier = Modifier.fillMaxSize().verticalScroll(scrollState)) {
                        // ----- Background/Banner -----
                        Box(modifier = Modifier.fillMaxWidth().height(220.dp)) {
                            if (movie.backdropPath != null) {
                                AsyncImage(
                                    model = "https://image.tmdb.org/t/p/w780${movie.backdropPath}",
                                    contentDescription = null,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )
                                Box(modifier = Modifier.fillMaxSize().background(Brush.verticalGradient(
                                    colors = listOf(Color.Transparent, MaterialTheme.colorScheme.background),
                                    startY = 80f
                                )))
                            }else {
                                Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.surfaceVariant))
                            }
                            if (trailer != null) {
                                IconButton(
                                    onClick = {
                                        viewModel.onEvent(MovieDetailsContract.Event.OpenYoutube(trailer.key))
                                    },
                                    modifier = Modifier
                                        .padding(16.dp)
                                        .align(Alignment.Center)
                                        .background(
                                            color = Color.Black.copy(alpha = 0.3f),
                                            shape = CircleShape
                                        )
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.PlayArrow,
                                        contentDescription = "Play",
                                        modifier = Modifier.size(48.dp),
                                        tint = Color.White
                                    )
                                }
                            }
                        }
                        Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp).offset(y = (-40).dp),
                            verticalAlignment = Alignment.Bottom,
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ){
                            // ----- Poster -----
                            Card(modifier = Modifier.size(100.dp),
                                shape = RoundedCornerShape(8.dp),
                                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)) {
                                if (movie.posterPath != null){
                                    AsyncImage(model = "https://image.tmdb.org/t/p/w342${movie.posterPath}",
                                        contentDescription = movie.title,
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier.fillMaxSize()
                                    )
                                } else {
                                    Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.surfaceVariant),
                                        contentAlignment = Alignment.Center) {
                                        Icon(
                                            Icons.Default.Movie,
                                            contentDescription = null,
                                            modifier = Modifier.size(40.dp),
                                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }
                            }

                            // ----- Heading -----
                            Column(modifier = Modifier.padding(bottom = 12.dp)) {
                                Text(
                                    text = movie.title,
                                    style = MaterialTheme.typography.titleLarge,
                                    maxLines = 3,
                                    overflow = TextOverflow.Ellipsis
                                )
                                if (movie.originalTitle != null && movie.originalTitle != movie.title) {
                                    Text(
                                        text = movie.originalTitle,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }

                                Spacer(modifier = Modifier.height(4.dp))

                                Row(verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    if (movie.year != null) {
                                        Text(
                                            text = movie.year.toString(),
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                    if (movie.year != null && movie.runtime != null) {
                                        Text(
                                            text = "-",
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                    if (movie.runtime != null) {
                                        Text(
                                            text = formatRuntime(movie.runtime),
                                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                                            style = MaterialTheme.typography.bodyMedium
                                        )
                                    }
                                }
                            }
                        }
                        Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
                            Spacer(modifier = Modifier.width(4.dp))

                            // ----- Content -----
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                verticalArrangement = Arrangement.spacedBy(24.dp)
                            ) {
                                if (movie.imdbRating != null || movie.tmdbRating != null){
                                    Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                                        movie.imdbRating?.let { rating ->
                                            RatingChip(label = "IMDb", rating, votes = movie.imdbVotes)
                                        }
                                        movie.tmdbRating?.let { rating ->
                                            RatingChip(label = "TMDb", rating, votes = movie.tmdbVotes)
                                        }
                                    }
                                }

                                if (movie.genres.isNotEmpty()) {
                                    FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp),
                                        modifier = Modifier.offset(y = (-16).dp)) {
                                        movie.genres.forEach { genre ->
                                            SuggestionChip(
                                                onClick = {},
                                                label = { Text(genre.name)}
                                            )
                                        }
                                    }
                                }

                                if(!movie.tagline.isNullOrBlank()){
                                    Text(text = "\"${movie.tagline}\"",
                                        style = MaterialTheme.typography.bodyMedium.copy(fontStyle = FontStyle.Italic),
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        modifier = Modifier.offset(y = (-16).dp))
                                }

                                if (!movie.overview.isNullOrBlank()) {
                                    SectionTitle("OVERVIEW")
                                    Text(text = movie.overview,
                                        style = MaterialTheme.typography.bodyMedium)
                                }

                                SectionTitle("DETAILS")
                                DetailsGrid(movie)

                                if (images.isNotEmpty()) {
                                    SectionTitle("IMAGES")
                                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp),
                                        modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState())){
                                        images.forEach { image ->
                                            Card(modifier = Modifier.height(100.dp),
                                                shape = RoundedCornerShape(8.dp),
                                                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)) {
                                                AsyncImage(
                                                    model = "https://image.tmdb.org/t/p/w300${image.filePath}}",
                                                    contentDescription = null,
                                                    contentScale = ContentScale.Crop,
                                                    modifier = Modifier.fillMaxSize()
                                                )
                                            }
                                        }
                                    }
                                }

                                if (screenState.cast.isNotEmpty()) {
                                    SectionTitle("CAST")
                                    CastRow(screenState.cast)
                                }

                                if (movie.collection != null) {
                                    SectionTitle("Part of a collection")
                                    CollectionCard(movie.collection)
                                }

                                Spacer(modifier = Modifier.height(32.dp))
                            }
                        }
                    }
                }
            }
            IconButton(
                onClick = { viewModel.onEvent(MovieDetailsContract.Event.BackClicked) },
                modifier = Modifier
                    .statusBarsPadding()
                    .padding(8.dp)
                    .align(Alignment.TopStart)
                    .background(
                        color = Color.Black.copy(alpha = 0.3f),
                        shape = CircleShape
                    )
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White
                )
            }
        }
    }
}

@Composable
private fun DetailsGrid(movie: MovieDTO) {
    val items = buildList {
        movie.languageCode?.let { add("Language" to it.uppercase()) }
        movie.budget?.takeIf { it > 0 }?.let { add("Budget" to formatMoney(it)) }
        movie.revenue?.takeIf { it > 0 }?.let { add("Revenue" to formatMoney(it)) }
        movie.popularity?.let { add("Popularity" to (round(it * 10f) / 10f).toString()) }
    }

    FlowRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items.forEach { (key, value) ->
            Surface(
                color = MaterialTheme.colorScheme.surfaceContainerHigh,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.wrapContentSize()
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Text(
                        text = key,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = value,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
private fun CastRow(cast: List<CastMemberDTO>) {
    LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        items(cast) { member ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.width(80.dp)
            ) {
                Card(
                    shape = CircleShape,
                    modifier = Modifier.size(64.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    if (member.profilePath != null) {
                        AsyncImage(
                            model = "https://image.tmdb.org/t/p/w185${member.profilePath}",
                            contentDescription = member.name,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    } else {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(MaterialTheme.colorScheme.surfaceVariant),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Default.Person,
                                contentDescription = null,
                                modifier = Modifier.size(32.dp),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
                Spacer(Modifier.height(6.dp))
                Text(
                    text = member.name,
                    style = MaterialTheme.typography.labelSmall,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

private fun formatRuntime(minutes: Int): String {
    val h = minutes / 60
    val m = minutes % 60
    return if (h > 0) "${h}h ${m}m" else "${m}min"
}

private fun formatMoney(amount: Long): String = when {
    amount >= 1_000_000_000 -> {
        val v = amount / 1_000_000_000.0
        "$${round(v)}B"
    }
    amount >= 1_000_000 -> {
        val v = amount / 1_000_000.0
        "$${round(v)}M"
    }
    else -> "$$amount"
}
