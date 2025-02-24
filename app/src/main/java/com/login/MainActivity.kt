package com.login

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModelProvider
import com.login.navigation.Navigation
import com.login.ui.screen.db.AppDatabase
import com.login.ui.screen.db.TaskRepo
import com.login.ui.screen.home.HomeViewModel
import com.login.ui.screen.home.HomeViewModelFactory
import com.login.ui.theme.LoginTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val database = AppDatabase.getDatabase(applicationContext)
        val repository = TaskRepo(database.taskDao())

        val viewModel = ViewModelProvider(this, HomeViewModelFactory(repository))[HomeViewModel::class.java]
        setContent {
            LoginTheme {
                Navigation(viewModel)
            }
        }
    }
}