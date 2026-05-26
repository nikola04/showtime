package rs.edu.raf.showtime.ui.screen.movielistfilter

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import org.koin.compose.viewmodel.koinViewModel
import rs.edu.raf.showtime.navigation.NavRoutes
import kotlin.math.round

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieListFiltersScreen(navController: NavController){
    val viewModel: MovieListFiltersViewModel = koinViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit){
        viewModel.effect.collect { effect -> when(effect) {
            is MovieListFiltersContract.Effect.NavigateBack -> {
                if (navController.previousBackStackEntry != null){
                    navController.popBackStack()
                }
            }
            is MovieListFiltersContract.Effect.ApplyFilters -> {
                navController.navigate(NavRoutes.MovieList.route)
            }
        } }
    }

    Scaffold(topBar = {
        TopAppBar(
            navigationIcon = {
                IconButton(
                    onClick = { viewModel.onEvent(MovieListFiltersContract.Event.BackClicked) },
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }
            },
            title = {
                Text("Filters", fontWeight = FontWeight.Black)
            },
            actions = {
                Button(
                    onClick = { viewModel.onEvent(MovieListFiltersContract.Event.ClearFiltersClicked) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    ),
                    shape = RoundedCornerShape(48)
                ) {
                    Icon(Icons.Default.Clear, contentDescription = null)
                    Spacer(Modifier.width(4.dp))
                    Text("Clear")
                }
            }
        )
    }) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            when (val screenState = state.screenState) {
                is MovieListFiltersContract.ScreenState.Loading -> {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
                is MovieListFiltersContract.ScreenState.Error -> {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Something went wrong", style = MaterialTheme.typography.bodyLarge)
                            Spacer(Modifier.height(8.dp))
                            Text(screenState.message, style = MaterialTheme.typography.bodySmall)
                            Spacer(Modifier.height(8.dp))
                            Button(onClick = { viewModel.onEvent(MovieListFiltersContract.Event.RetryClicked) }) {
                                Text("Retry")
                            }
                        }
                    }
                }
                is MovieListFiltersContract.ScreenState.Success -> {
                    val filters = state.activeFilters
                    val genres = screenState.genres
                    Column(modifier = Modifier.padding(horizontal = 16.dp).fillMaxWidth().verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        ) {

                        SectionTitle("SEARCH")
                        TextField(
                            value = filters.query ?: "",
                            onValueChange = { text ->
                                viewModel.onEvent(
                                    MovieListFiltersContract.Event.QueryChanged(
                                        text
                                    )
                                )
                            },
                            maxLines = 1,
                            modifier = Modifier.fillMaxWidth()
                        )

                        SectionTitle("GENRE")
                        FlowRow(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            genres.forEach { genre ->
                                FilterChip(
                                    label = { Text(text = genre.name) },
                                    onClick = { viewModel.onEvent(MovieListFiltersContract.Event.GenreChanged(genre.id)) },
                                    selected = filters.genreId == genre.id,
                                )
                            }
                        }

                        SectionTitle("YEAR RANGE")
                        YearRangeFilter(
                            minYear = filters.minYear,
                            maxYear = filters.maxYear,
                            onRangeChanged = { min, max -> viewModel.onEvent(MovieListFiltersContract.Event.YearRangeChanged( minYear = min, maxYear = max)) }
                        )

                        SectionTitle("RATING")
                        RatingFilter(
                            minRating = filters.minRating,
                            onRatingChanged = { rating -> viewModel.onEvent(MovieListFiltersContract.Event.MinRatingChanged(rating)) }
                        )

                        Button(
                            onClick = { viewModel.onEvent(MovieListFiltersContract.Event.ApplyClicked) },
                            shape = RoundedCornerShape(48),
                            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
                        ){
                            Text(text = "Apply filters", fontWeight = FontWeight.Medium)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SectionTitle(title: String) {
    Text(text = title,
        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Black),
        modifier = Modifier.padding(bottom = 4.dp))
}

@Composable
fun YearRangeFilter(
    minYear: Int?,
    maxYear: Int?,
    onRangeChanged: (Int, Int) -> Unit
) {
    val rangeLimits = 1900f..2030f

    val currentRange = (minYear?.toFloat() ?: 1900f)..(maxYear?.toFloat() ?: 2030f)

    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp)) {
        Text(
            text = "Year Range: ${currentRange.start.toInt()} - ${currentRange.endInclusive.toInt()}",
            style = MaterialTheme.typography.labelLarge
        )

        RangeSlider(
            value = currentRange,
            onValueChange = { range ->
                onRangeChanged(range.start.toInt(), range.endInclusive.toInt())
            },
            valueRange = rangeLimits,
            steps = 0
        )
    }
}

@Composable
fun RatingFilter(
    minRating: Float?,
    onRatingChanged: (Float) -> Unit
) {
    val rating = minRating ?: 0f

    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp)) {
        Text(
            text = "Minimum Rating: ${round(rating * 10f) / 10f}",
            style = MaterialTheme.typography.labelLarge
        )

        Slider(
            value = rating,
            onValueChange = onRatingChanged,
            valueRange = 0f..10f,
            steps = 0
        )
    }
}