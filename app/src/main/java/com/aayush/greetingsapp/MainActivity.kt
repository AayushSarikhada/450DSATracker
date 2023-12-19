package com.aayush.greetingsapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import org.apache.commons.csv.CSVFormat


private val listOfDSAProblems = arrayListOf<DSAProblem>()

class MainActivity : ComponentActivity() {

    // variables
    private val db = Firebase.firestore

    // life cycle functions
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GreetingsAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    readProblemCSVFile()
                    val temp = DSAProblem(1,"aaa","bbb",true)
                    db.collection("users")
                        .add(temp)
                        .addOnSuccessListener { documentReference ->
                            Log.d("TAG", "DocumentSnapshot added with ID: ${documentReference.id}")
                        }
                        .addOnFailureListener { e ->
                            Log.w("TAG", "Error adding document", e)
                        }
                    LazyColumn {
                        items(listOfDSAProblems) {
                            RowDSAProblems(problem = it)
                        }
                    }

                }
            }
        }
    }

    // util functions
    private fun readProblemCSVFile() {
        var id = 0
        val inputStream = resources.openRawResource(R.raw.fin)
        val bufferedReader = inputStream.bufferedReader()
        CSVFormat.Builder.create(CSVFormat.RFC4180).apply {
            setIgnoreHeaderCase(true)
            setIgnoreSurroundingSpaces(true)
            setHeader(DSAProblemsCSVHeader::class.java)
        }
            .build()
            .parse(bufferedReader)
            .drop(1)
            .map {
                val pType = it.get(DSAProblemsCSVHeader.TYPE)
                val pName = it.get(DSAProblemsCSVHeader.PROBLEM)
                val pDone = it.get(DSAProblemsCSVHeader.DONE)

                listOfDSAProblems.add(
                    DSAProblem(
                        id = id++,
                        pType,
                        pName,
                        pDone == "YES"
                    )
                )
            }
    }

}

@Composable
fun RowDSAProblems(problem: DSAProblem) {

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
                    listOfDSAProblems[problem.id].problemDone = it
                    checked = it
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

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Surface(color = Color.DarkGray) {
        RowDSAProblems(DSAProblem(0, "Array", "Solve the Array", true))
    }
}