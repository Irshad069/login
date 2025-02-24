package com.login.ui.screen.home

import com.login.ui.screen.db.model.Task

sealed class HomeState {
    data object Loading : HomeState()
    data class Success(val tasks: List<Task>) : HomeState()
    data class Error(val message: String) : HomeState()
}

sealed class HomeIntent {
    data class AddTask(val task: Task) : HomeIntent()
    data class UpdateTask(val task: Task) : HomeIntent()
    data class DeleteTask(val task: Task) : HomeIntent()
    data object LoadTasks : HomeIntent()
}
