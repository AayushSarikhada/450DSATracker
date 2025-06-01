package com.aayush.dsa450.presentation.problemlist

import com.aayush.dsa450.domain.model.DSAProblem

data class ProblemListState(
    val problems: List<DSAProblem> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val completedCount: Int = 0,
    val totalCount: Int = 0,
    val progressPercentage: Float = 0f,
    val selectedFilter: String = "All",
    val searchQuery: String = ""
) {
    val filteredProblems: List<DSAProblem>
        get() = problems.filter { problem ->
            val matchesFilter = selectedFilter == "All" || problem.problemType == selectedFilter
            val matchesSearch = searchQuery.isEmpty() || 
                problem.problemName.contains(searchQuery, ignoreCase = true) ||
                problem.problemType.contains(searchQuery, ignoreCase = true)
            matchesFilter && matchesSearch
        }
    
    val availableFilters: List<String>
        get() = listOf("All") + problems.map { it.problemType }.distinct().sorted()
}
