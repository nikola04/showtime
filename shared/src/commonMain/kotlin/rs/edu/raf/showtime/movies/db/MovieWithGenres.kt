package rs.edu.raf.showtime.movies.db

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class MovieWithGenres (
    @Embedded val movie: MovieEntity,

    @Relation(
        parentColumn = "imdbId",
        entityColumn = "id",
        associateBy = Junction(MovieGenreCrossRef::class)
    ) val genres: List<GenreEntity>
)