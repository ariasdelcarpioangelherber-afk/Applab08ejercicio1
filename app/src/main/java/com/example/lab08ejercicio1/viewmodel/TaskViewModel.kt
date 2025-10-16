package com.example.lab08ejercicio1.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

import com.example.lab08ejercicio1.Task
import com.example.lab08ejercicio1.TaskDao

enum class TaskFilter { ALL, PENDING, COMPLETED }

class TaskViewModel(private val dao: TaskDao) : ViewModel() {

    private val _allTasks = MutableStateFlow<List<Task>>(emptyList())
    private val _filter = MutableStateFlow(TaskFilter.ALL)
    val filter: StateFlow<TaskFilter> = _filter

    // El StateFlow que la UI observa (se actualiza automáticamente al cambiar _allTasks o _filter)
    val tasks: StateFlow<List<Task>> = _allTasks
        .combine(_filter) { allTasks, currentFilter ->
            when (currentFilter) {
                TaskFilter.ALL -> allTasks
                TaskFilter.PENDING -> allTasks.filter { !it.isCompleted }
                TaskFilter.COMPLETED -> allTasks.filter { it.isCompleted }
            }
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

    init {
        loadTasks()
    }

    private fun loadTasks() {
        viewModelScope.launch {
            _allTasks.value = dao.getAllTasks()
        }
    }

    fun addTask(description: String) {
        viewModelScope.launch {
            dao.insertTask(Task(description = description))
            loadTasks()
        }
    }

    fun toggleTaskCompletion(task: Task) {
        viewModelScope.launch {
            val updatedTask = task.copy(isCompleted = !task.isCompleted)
            dao.updateTask(updatedTask)
            loadTasks()
        }
    }

    fun deleteTask(task: Task) { // Eliminación Individual
        viewModelScope.launch {
            dao.deleteTask(task)
            loadTasks()
        }
    }

    fun deleteAllTasks() {
        viewModelScope.launch {
            dao.deleteAllTasks()
            _allTasks.value = emptyList()
        }
    }

    fun setFilter(newFilter: TaskFilter) { // Cambio de Filtro
        _filter.value = newFilter
    }
}