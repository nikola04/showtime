package rs.edu.raf.showtime.ui.screen.movielist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import org.koin.compose.viewmodel.koinViewModel
import rs.edu.raf.showtime.data.model.MovieMinDTO
import rs.edu.raf.showtime.navigation.NavRoutes
import rs.edu.raf.showtime.ui.screen.movielist.MovieListContract.Event
import rs.edu.raf.showtime.ui.screen.movielist.MovieListContract.Effect
import rs.edu.raf.showtime.ui.screen.movielist.MovieListContract.SortOption
import rs.edu.raf.showtime.ui.screen.movielist.MovieListContract.SortOrder

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieListScreen(navController: NavController) {
    val viewModel: MovieListViewModel = koinViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is Effect.NavigateToDetails ->
                    navController.navigate(NavRoutes.MovieDetails().createRoute(effect.movieId))
                is Effect.NavigateToFilter ->
                    navController.navigate(NavRoutes.MovieListFilters.route)
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Premiere", fontWeight = FontWeight.Black)
                },
                actions = {
                    Button(
                        onClick = { viewModel.onEvent(Event.FilterButtonClicked) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        ),
                        shape = RoundedCornerShape(50)
                    ) {
                        Icon(Icons.Default.Tune, contentDescription = "Filter")
                        Spacer(Modifier.width(4.dp))
                        Text("Filter")
                    }
                    Badge(
                        containerColor = if (state.activeFilterCount > 0)
                            MaterialTheme.colorScheme.error else Color.Transparent,
                        modifier = Modifier.offset(x = (-12).dp, y = (-10).dp),
                    ) {
                        if (state.activeFilterCount > 0) {
                            Text("${state.activeFilterCount}",
                                fontWeight = FontWeight.ExtraBold)
                        }
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                SortPill(
                    currentSort = state.sortBy,
                    onSortChanged = { viewModel.onEvent(Event.SortChanged(it)) }
                )
                if (state.screenState is MovieListContract.ScreenState.Success) {
                    Text(
                        text = "${(state.screenState as MovieListContract.ScreenState.Success).total} movies",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            when (val screenState = state.screenState) {
                is MovieListContract.ScreenState.Loading -> {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
                is MovieListContract.ScreenState.Error -> {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Something went wrong", style = MaterialTheme.typography.bodyLarge)
                            Spacer(Modifier.height(8.dp))
                            Text(screenState.message, style = MaterialTheme.typography.bodySmall)
                            Spacer(Modifier.height(8.dp))
                            Button(onClick = { viewModel.onEvent(Event.RetryClicked) }) {
                                Text("Retry")
                            }
                        }
                    }
                }
                is MovieListContract.ScreenState.Empty -> {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("No movies found")
                    }
                }
                is MovieListContract.ScreenState.Success -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(horizontal = 12.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Spacer(Modifier.height(4.dp))
                        screenState.movies.forEach { movie ->
                            MovieListItem(
                                movie = movie,
                                onClick = { viewModel.onEvent(Event.MovieClicked(movie.imdbId)) }
                            )
                        }
                        Spacer(Modifier.height(12.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun SortPill(
    currentSort: SortOption,
    onSortChanged: (SortOption) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        FilterChip(
            selected = true,
            onClick = { expanded = true },
            label = {
                Text("Sort: ${currentSort.label}")
            },
            trailingIcon = {
                Icon(
                    imageVector = if (currentSort.order == SortOrder.ASC) Icons.Default.ArrowUpward else Icons.Default.ArrowDownward,
                    contentDescription = null,
                    modifier = Modifier.size(14.dp)
                )
            }
        )
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            SortOption.entries.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option.label) },
                    onClick = {
                        onSortChanged(option)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun MovieListItem(
    movie: MovieMinDTO,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        Row(modifier = Modifier.padding(10.dp)) {
            AsyncImage(
                model = "https://image.tmdb.org/t/p/w185${movie.posterPath}",
                contentDescription = movie.title,
                modifier = Modifier
                    .width(80.dp)
                    .height(110.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = movie.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = "${movie.year ?: ""}${if (movie.runtime != null) " · ${movie.runtime} min" else ""}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(Modifier.height(6.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.Star,
                        contentDescription = null,
                        tint = Color(0xFFFFC107),
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(
                        text = "${movie.imdbRating ?: "-"}",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFFFC107)
                    )
                    Spacer(Modifier.width(6.dp))
                    Text(
                        text = formatVotes(movie.imdbVotes),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Spacer(Modifier.height(4.dp))
                Row(
                    modifier = Modifier.horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    movie.genres.forEach { genre ->
                        SuggestionChip(
                            onClick = {},
                            colors = SuggestionChipDefaults.suggestionChipColors(
                                containerColor = MaterialTheme.colorScheme.background,
                                labelColor = MaterialTheme.colorScheme.onBackground
                            ),
                            label = { Text(genre.name, style = MaterialTheme.typography.labelSmall) }
                        )
                    }
                }
            }
        }
    }
}

private fun formatVotes(votes: Int?): String {
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