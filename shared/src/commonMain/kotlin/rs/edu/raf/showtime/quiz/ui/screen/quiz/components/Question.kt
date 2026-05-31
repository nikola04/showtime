package rs.edu.raf.showtime.quiz.ui.screen.quiz.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import rs.edu.raf.showtime.quiz.domain.QuestionType
import rs.edu.raf.showtime.quiz.domain.QuizQuestion

@Composable
fun Question(
    question: QuizQuestion,
    selectedAnswer: String?,
    isAnswered: Boolean,
    selectAnswer: (answer: String) -> Unit
){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = questionTitle(question),
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(16.dp))

        question.movieTitle?.let {
            Text(
                text = it,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(Modifier.height(12.dp))
        }

        question.imageUrl?.let { url ->

            AsyncImage(
                model = "https://image.tmdb.org/t/p/w780${url}",
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(16.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(Modifier.height(24.dp))
        }

        question.answers.forEach { answer ->
            AnswerButton(
                answer = answer,
                correctAnswer = question.correctAnswer,
                selectedAnswer = selectedAnswer,
                isAnswered = isAnswered,
                onClick = { selectAnswer(answer) }
            )

            Spacer(Modifier.height(12.dp))
        }
    }
}

@Composable
private fun questionTitle(
    question: QuizQuestion
): String {
    return when (question.type) {
        QuestionType.GUESS_MOVIE ->
            "Which movie is shown?"

        QuestionType.GUESS_YEAR ->
            "Which year was movie released?"

        QuestionType.GUESS_LEAD_ACTOR ->
            "Who is the lead actor?"
    }
}