package com.aayush.dsa450.presentation.accessibility

import androidx.compose.foundation.clickable
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.CustomAccessibilityAction
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.customActions
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import com.aayush.dsa450.R
import com.aayush.dsa450.domain.model.DSAProblem

/**
 * Accessibility helper functions for the DSA450 app
 */

@Composable
fun Modifier.problemItemAccessibility(
    problem: DSAProblem,
    onToggleStatus: () -> Unit
): Modifier {
    val context = LocalContext.current
    
    val statusDescription = if (problem.isDone) {
        stringResource(R.string.problem_completed_description)
    } else {
        stringResource(R.string.problem_pending_description)
    }
    
    val difficultyDescription = when (problem.difficulty) {
        DSAProblem.Difficulty.EASY -> stringResource(R.string.difficulty_easy_description)
        DSAProblem.Difficulty.MEDIUM -> stringResource(R.string.difficulty_medium_description)
        DSAProblem.Difficulty.HARD -> stringResource(R.string.difficulty_hard_description)
    }
    
    val toggleAction = if (problem.isDone) {
        "Mark as pending"
    } else {
        "Mark as completed"
    }
    
    return this.semantics {
        contentDescription = "${problem.problemName}. $difficultyDescription. $statusDescription"
        stateDescription = statusDescription
        role = Role.Button
        
        customActions = listOf(
            CustomAccessibilityAction(
                label = toggleAction,
                action = {
                    onToggleStatus()
                    true
                }
            )
        )
    }.clickable(
        onClickLabel = toggleAction,
        role = Role.Button
    ) {
        onToggleStatus()
    }
}

@Composable
fun Modifier.loadingAccessibility(): Modifier {
    val loadingDescription = stringResource(R.string.loading_description)
    return this.semantics {
        contentDescription = loadingDescription
    }
}

@Composable
fun Modifier.errorStateAccessibility(errorMessage: String): Modifier {
    return this.semantics {
        contentDescription = "Error: $errorMessage"
        role = Role.Button
    }
}

@Composable
fun Modifier.searchFieldAccessibility(): Modifier {
    val searchHint = stringResource(R.string.search_hint)
    return this.semantics {
        contentDescription = searchHint
    }
}

/**
 * Helper function to announce important updates to screen readers
 */
fun announceForAccessibility(context: android.content.Context, message: String) {
    val view = context as? androidx.activity.ComponentActivity
    view?.findViewById<android.view.View>(android.R.id.content)?.announceForAccessibility(message)
}
