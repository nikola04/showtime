package rs.edu.raf.showtime.quiz.ui.screen.quiz.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AnswerButton(
    answer: String,
    correctAnswer: String,
    selectedAnswer: String?,
    isAnswered: Boolean,
    onClick: () -> Unit,
) {

    val containerColor =
        when {
            !isAnswered ->
                MaterialTheme.colorScheme.surfaceContainer

            answer == correctAnswer ->
                MaterialTheme.colorScheme.primaryContainer

            answer == selectedAnswer ->
                MaterialTheme.colorScheme.errorContainer

            else ->
                MaterialTheme.colorScheme.surfaceContainer
        }

    val contentColor =
        when {
            !isAnswered ->
                MaterialTheme.colorScheme.onSurface

            answer == correctAnswer ->
                MaterialTheme.colorScheme.onPrimaryContainer

            answer == selectedAnswer ->
                MaterialTheme.colorScheme.onErrorContainer

            else ->
                MaterialTheme.colorScheme.onSurface
        }

    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        onClick = {
            if (!isAnswered) {
                onClick()
            }
        },
        colors = CardDefaults.elevatedCardColors(
            containerColor = containerColor,
            contentColor = contentColor
        )
    ) {
        Text(
            text = answer,
            modifier = Modifier.padding(20.dp),
            style = MaterialTheme.typography.titleMedium
        )
    }
}