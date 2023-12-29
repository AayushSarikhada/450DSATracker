package com.aayush.greetingsapp.ui.components

import android.util.Log
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.aayush.greetingsapp.model.DSAProblem
import com.aayush.greetingsapp.viewmodel.DSAProblemVM

@Composable
fun DSAProblemList(
    listOfProblems: SnapshotStateList<DSAProblem>,
    viewModel: DSAProblemVM
) {
    LazyColumn {
        items(listOfProblems) {
            Log.d("LAZY COLUMN", "${it.id}")
            RowDSAProblems(problem = it, viewModel)
        }
    }

}