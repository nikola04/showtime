package rs.edu.raf.showtime.movies.db

import androidx.room.Embedded
import androidx.room.Relation

data class MovieWithCollection(
    @Embedded
    val movie: MovieEntity,

    @Relation(
        parentColumn = "collectionId",
        entityColumn = "id"
    )
    val collection: CollectionEntity?
)
