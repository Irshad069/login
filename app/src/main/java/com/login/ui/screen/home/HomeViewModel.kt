package com.login.ui.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.login.ui.screen.db.TaskRepo
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: TaskRepo) : ViewModel() {

    val taskIntent = Channel<HomeIntent>(Channel.UNLIMITED)

    // Collect tasks from DB
    private val _taskState = MutableStateFlow<HomeState>(HomeState.Loading)
    val taskState: StateFlow<HomeState> = _taskState

    init {
        collectTasks()  // Load tasks when ViewModel initializes
        handleIntent()
    }

    private fun collectTasks() {
        viewModelScope.launch {
            repository.allTasks.collect { tasks ->
                _taskState.value = HomeState.Success(tasks)
            }
        }
    }

    private fun handleIntent() {
        viewModelScope.launch {
            taskIntent.consumeAsFlow().collect { intent ->
                when (intent) {
                    is HomeIntent.AddTask -> {
                        repository.insertTask(intent.task)
                    }

                    is HomeIntent.UpdateTask -> {
                        repository.updateTask(intent.task)
                    }

                    is HomeIntent.DeleteTask -> {
                        repository.deleteTask(intent.task)
                    }

                    is HomeIntent.LoadTasks -> {
                        collectTasks() // Reload tasks when requested
                    }
                }
            }
        }
    }
}
