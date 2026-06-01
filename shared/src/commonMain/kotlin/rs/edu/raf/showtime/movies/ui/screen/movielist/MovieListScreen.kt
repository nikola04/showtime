package rs.edu.raf.showtime.movies.ui.screen.movielist

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import rs.edu.raf.showtime.movies.ui.screen.movielist.components.MovieListItem
import rs.edu.raf.showtime.movies.ui.screen.movielist.MovieListContract.Event
import rs.edu.raf.showtime.movies.ui.screen.movielist.MovieListContract.Effect
import rs.edu.raf.showtime.movies.ui.screen.movielist.components.SortPill

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieListScreen(
    viewModel: MovieListViewModel,
    onMovieClick: (String) -> Unit,
    onFiltersClick: () -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is Effect.NavigateToDetails ->
                    onMovieClick(effect.movieId)
                is Effect.NavigateToFilter ->
                    onFiltersClick()
                is Effect.ShowError -> {
                    scope.launch {
                        snackbarHostState.showSnackbar(effect.message)
                    }
                }
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Text("Showtime", fontWeight = FontWeight.Black)
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
                .padding(top=padding.calculateTopPadding())
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
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
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 12.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        item { Spacer(Modifier.height(4.dp)) }
                        items(screenState.movies) { movie ->
                            MovieListItem(
                                movie = movie,
                                onClick = { viewModel.onEvent(Event.MovieClicked(movie.imdbId)) }
                            )
                        }
                        item { Spacer(Modifier.height(12.dp)) }
                    }
                }
            }
        }
    }
}

