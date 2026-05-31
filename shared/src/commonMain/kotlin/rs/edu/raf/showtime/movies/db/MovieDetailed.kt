package rs.edu.raf.showtime.movies.db

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class MovieDetailed(
    @Embedded val movie: MovieEntity,

    @Relation(
        parentColumn = "collectionId",
        entityColumn = "id"
    ) val collection: CollectionEntity?,

    @Relation(
        parentColumn = "imdbId",
        entityColumn = "id",
        associateBy = Junction(
            value = MovieGenreCrossRef::class,
            parentColumn = "imdbId",
            entityColumn = "genreId"
        )
    ) val genres: List<GenreEntity>
)
