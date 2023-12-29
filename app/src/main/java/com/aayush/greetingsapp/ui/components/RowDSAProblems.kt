package com.aayush.greetingsapp.ui.components

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.aayush.greetingsapp.model.DSAProblem
import com.aayush.greetingsapp.ui.theme.Typography
import com.aayush.greetingsapp.viewmodel.DSAProblemVM


@Composable
fun RowDSAProblems(
    problem: DSAProblem,
    viewModel: DSAProblemVM
) {

    var checked by remember { mutableStateOf(problem.problemDone) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(
                text = problem.problemType,
                style = Typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Column(
            Modifier
                .weight(0.6f)
        ) {
            Text(
                text = problem.problemName,
                style = Typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground,
                maxLines = 1,
                textAlign = TextAlign.Center,
                overflow = TextOverflow.Ellipsis
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            IconToggleButton(checked = checked,
                onCheckedChange = {
                    viewModel.dsaProblems[problem.id].problemDone = it
                    checked = it
                    Log.d("onCheckedChange", "problem no. ${problem.id} done!!!")
                    viewModel.updateFireBaseData(problem.id)
                }) {
                Icon(
                    Icons.Outlined.CheckCircle,
                    "Check icon",
                    tint = if (checked) Color.Green else Color.DarkGray
                )
            }
        }
    }

}