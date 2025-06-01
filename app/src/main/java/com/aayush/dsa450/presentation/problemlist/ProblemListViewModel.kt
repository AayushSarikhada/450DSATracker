package com.aayush.dsa450.presentation.problemlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aayush.dsa450.domain.model.Resource
import com.aayush.dsa450.domain.usecase.GetAllProblemsUseCase
import com.aayush.dsa450.domain.usecase.GetProgressUseCase
import com.aayush.dsa450.domain.usecase.UpdateProblemStatusUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ProblemListViewModel @Inject constructor(
    private val getAllProblemsUseCase: GetAllProblemsUseCase,
    private val updateProblemStatusUseCase: UpdateProblemStatusUseCase,
    private val getProgressUseCase: GetProgressUseCase
) : ViewModel() {
    
    private val _state = MutableStateFlow(ProblemListState())
    val state: StateFlow<ProblemListState> = _state.asStateFlow()
    
    init {
        loadProblems()
        observeProgress()
    }
    
    fun onEvent(event: ProblemListEvent) {
        when (event) {
            is ProblemListEvent.LoadProblems -> loadProblems()
            is ProblemListEvent.UpdateProblemStatus -> updateProblemStatus(event.problemId, event.isDone)
            is ProblemListEvent.FilterByType -> updateFilter(event.type)
            is ProblemListEvent.SearchProblems -> updateSearchQuery(event.query)
            is ProblemListEvent.ClearError -> clearError()
            is ProblemListEvent.ImportFromCsv -> importFromCsv()
            is ProblemListEvent.SyncWithRemote -> syncWithRemote()
        }
    }
    
    private fun loadProblems() {
        viewModelScope.launch {
            getAllProblemsUseCase().collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        _state.value = _state.value.copy(isLoading = true, error = null)
                    }
                    is Resource.Success -> {
                        _state.value = _state.value.copy(
                            problems = result.data ?: emptyList(),
                            isLoading = false,
                            error = null
                        )
                    }
                    is Resource.Error -> {
                        _state.value = _state.value.copy(
                            isLoading = false,
                            error = result.message
                        )
                        Timber.e("Error loading problems: ${result.message}")
                    }
                }
            }
        }
    }
    
    private fun updateProblemStatus(problemId: Int, isDone: Boolean) {
        viewModelScope.launch {
            val result = updateProblemStatusUseCase(problemId, isDone)
            when (result) {
                is Resource.Error -> {
                    _state.value = _state.value.copy(error = result.message)
                    Timber.e("Error updating problem status: ${result.message}")
                }
                else -> {
                    // Success handled by the flow from getAllProblems
                    Timber.d("Problem $problemId status updated to $isDone")
                }
            }
        }
    }
    
    private fun observeProgress() {
        viewModelScope.launch {
            combine(
                getProgressUseCase.getCompletedCount(),
                getProgressUseCase.getTotalCount(),
                getProgressUseCase.getProgressPercentage()
            ) { completed, total, percentage ->
                _state.value = _state.value.copy(
                    completedCount = completed,
                    totalCount = total,
                    progressPercentage = percentage
                )
            }.collect { }
        }
    }
    
    private fun updateFilter(type: String) {
        _state.value = _state.value.copy(selectedFilter = type)
    }
    
    private fun updateSearchQuery(query: String) {
        _state.value = _state.value.copy(searchQuery = query)
    }
    
    private fun clearError() {
        _state.value = _state.value.copy(error = null)
    }
    
    private fun importFromCsv() {
        // Implementation would call repository import method
        viewModelScope.launch {
            Timber.d("Import from CSV requested")
            // Add implementation
        }
    }
    
    private fun syncWithRemote() {
        // Implementation would call repository sync method
        viewModelScope.launch {
            Timber.d("Sync with remote requested")
            // Add implementation
        }
    }
}
