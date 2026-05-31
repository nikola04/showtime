package rs.edu.raf.showtime.quiz.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface QuizDao {
    @Query(
        """
        SELECT *
        FROM quizResults
        ORDER BY score DESC, id DESC
        LIMIT 1
    """
    )
    fun getBestQuizResult(): Flow<QuizResultEntity?>

    @Query("""
        SELECT COUNT(id)
        FROM quizResults
    """)
    fun getTotalQuizResults(): Flow<Long>

    @Insert
    suspend fun insertQuizResult(r: QuizResultEntity)
}