package com.aayush.dsa450.presentation.problemlist

sealed class ProblemListEvent {
    object LoadProblems : ProblemListEvent()
    object ImportFromCsv : ProblemListEvent()
    object SyncWithRemote : ProblemListEvent()
    data class UpdateProblemStatus(val problemId: Int, val isDone: Boolean) : ProblemListEvent()
    data class FilterByType(val type: String) : ProblemListEvent()
    data class SearchProblems(val query: String) : ProblemListEvent()
    object ClearError : ProblemListEvent()
}
