package com.aayush.greetingsapp.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import com.aayush.greetingsapp.R
import com.aayush.greetingsapp.model.DSAProblem
import com.aayush.greetingsapp.others.DSAProblemsCSVHeader
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import org.apache.commons.csv.CSVFormat

class DSAProblemVM {
    val dsaProblems = mutableStateListOf<DSAProblem>()
    val isLoading = mutableStateOf(true)
    private val db = Firebase.firestore

    init {
        readFromFirebase()
    }

    private fun readFromFirebase() {
        db.collection("problems")
            .orderBy("id")
            .get()
            .addOnSuccessListener { listOfQuerySnapshot ->
                listOfQuerySnapshot.documents.forEach { querySnapshot ->
                    Log.d("querySnapshot", querySnapshot.toString())
                    querySnapshot.toObject(DSAProblem::class.java)
                        ?.let { dsaProblems.add(it) }
                }
                isLoading.value = false
            }
            .addOnFailureListener {
                Log.d("onReadFromCacheFailure", "read from server failed with exception: $it")
            }
    }

    fun addAllToFirebase() {
        for (problem in dsaProblems) {
            db.collection("problems")
                .document("${problem.id}")
                .set(problem)
                .addOnSuccessListener {
                    Log.d("FIREBASE", "DocumentSnapshot added!!")
                }
                .addOnFailureListener { e ->
                    Log.w("FIREBASE", "Error adding document", e)
                }
        }

    }

    fun updateFireBaseData(problemId: Int) {
        db.collection("problems")
            .document("$problemId")
            .set(dsaProblems[problemId], SetOptions.merge())
            .addOnSuccessListener {
                Log.d("onUpdateSuccess", "problem no. $problemId updated successfully!!")
            }
            .addOnFailureListener {
                Log.d(
                    "onUpdateSuccess",
                    "problem no. $problemId updated failed with exception:  $it"
                )
            }
    }

//    private fun readProblemCSVFile() {
//        var id = 0
//        val inputStream = resources.openRawResource(R.raw.fin)
//        val bufferedReader = inputStream.bufferedReader()
//        CSVFormat.Builder.create(CSVFormat.RFC4180).apply {
//            setIgnoreHeaderCase(true)
//            setIgnoreSurroundingSpaces(true)
//            setHeader(DSAProblemsCSVHeader::class.java)
//        }
//            .build()
//            .parse(bufferedReader)
//            .drop(1)
//            .map {
//                val pType = it.get(DSAProblemsCSVHeader.TYPE)
//                val pName = it.get(DSAProblemsCSVHeader.PROBLEM)
//                val pDone = it.get(DSAProblemsCSVHeader.DONE)
//                Log.d("UPLOAD","ID $id")
//                dsaProblems.add(
//                    DSAProblem(
//                        id = id++,
//                        pType,
//                        pName,
//                        pDone == "YES"
//                    )
//                )
//            }
//        addAllToFirebase()
//    }
}