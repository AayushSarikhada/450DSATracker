package com.aayush.greetingsapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.aayush.greetingsapp.model.DSAProblem
import com.aayush.greetingsapp.others.DSAProblemsCSVHeader
import com.aayush.greetingsapp.ui.theme.GreetingsAppTheme
import com.aayush.greetingsapp.ui.theme.Typography
import com.aayush.greetingsapp.viewmodel.DSAProblemVM
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import org.apache.commons.csv.CSVFormat


class MainActivity : ComponentActivity() {

    // life cycle functions
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GreetingsAppTheme {
                DSAProblemListScreen()
            }
        }
    }

}

@Composable
fun LoadingLottieAnimation(modifier: Modifier) {

    val loadingComposition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(
            R.raw.loading
        )
    )

    LottieAnimation(
        composition = loadingComposition,
        iterations = LottieConstants.IterateForever,
        isPlaying = true,
        modifier = modifier
    )

}

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

@Composable
fun SetStatusBarColor(color: Color) {
    val systemUiController = rememberSystemUiController()
    SideEffect {
        systemUiController.setStatusBarColor(color)
    }
}

@Composable
fun DSAProblemListScreen(
    viewModel: DSAProblemVM = DSAProblemVM()
) {
    // A surface container using the 'background' color from the theme
    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Box {
            if(viewModel.isLoading.value) {
                LoadingLottieAnimation(modifier = Modifier.align(Alignment.Center))
            } else {
                SetStatusBarColor(color = Color.Green)
                DSAProblemList(viewModel.dsaProblems, viewModel)
            }
        }

    }
}

@Composable
fun DSAProblemList(
    listOfProblems: SnapshotStateList<DSAProblem>,
    viewModel: DSAProblemVM
) {
    Log.d("MAIN", listOfProblems.size.toString())
    LazyColumn {
        items(listOfProblems) {
            Log.d("LAZY COLUMN", "${it.id}")
            RowDSAProblems(problem = it, viewModel)
        }
    }

}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Surface(color = Color.DarkGray) {
//        RowDSAProblems(DSAProblem(0, "Array", "Solve the Array", true))
    }
}