package com.aayush.dsa450.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.RadioButtonUnchecked
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.aayush.dsa450.domain.model.DSAProblem
import com.aayush.dsa450.presentation.accessibility.problemItemAccessibility
import com.aayush.dsa450.presentation.theme.CompletedGreen
import com.aayush.dsa450.presentation.theme.EasyColor
import com.aayush.dsa450.presentation.theme.HardColor
import com.aayush.dsa450.presentation.theme.MediumColor

@Composable
fun ProblemItem(
    problem: DSAProblem,
    onStatusChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {    Card(
        modifier = modifier
            .fillMaxWidth()
            .problemItemAccessibility(
                problem = problem,
                onToggleStatus = { onStatusChange(!problem.isDone) }
            ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (problem.isDone) {
                MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
            } else {
                MaterialTheme.colorScheme.surface
            }
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                // Problem Type with Difficulty Badge
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    DifficultyBadge(difficulty = problem.difficulty)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = problem.problemType,
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Medium
                    )
                }
                
                Spacer(modifier = Modifier.height(4.dp))
                
                // Problem Name
                Text(
                    text = problem.problemName,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    color = if (problem.isDone) {
                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    } else {
                        MaterialTheme.colorScheme.onSurface
                    }
                )
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
                Icon(
                imageVector = if (problem.isDone) {
                    Icons.Outlined.CheckCircle
                } else {
                    Icons.Outlined.RadioButtonUnchecked
                },
                contentDescription = if (problem.isDone) "Completed" else "Not completed",
                tint = if (problem.isDone) CompletedGreen else MaterialTheme.colorScheme.outline
            )
        }
    }
}

@Composable
private fun DifficultyBadge(
    difficulty: DSAProblem.Difficulty,
    modifier: Modifier = Modifier
) {
    val (color, text) = when (difficulty) {
        DSAProblem.Difficulty.EASY -> EasyColor to "E"
        DSAProblem.Difficulty.MEDIUM -> MediumColor to "M"
        DSAProblem.Difficulty.HARD -> HardColor to "H"
    }
    
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(4.dp),
        color = color
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
            style = MaterialTheme.typography.labelSmall,
            color = Color.White,
            fontWeight = FontWeight.Bold
        )
    }
}
