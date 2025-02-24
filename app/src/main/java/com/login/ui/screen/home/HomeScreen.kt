package com.login.ui.screen.home

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.VisualTransformation
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.login.R
import com.login.navigation.Routes
import com.login.ui.screen.components.CustomButton
import com.login.ui.screen.components.CustomOutlinedTextField
import com.login.ui.screen.db.model.Task
import com.login.ui.screen.dialog.DatePickerDialog
import network.chaintech.sdpcomposemultiplatform.sdp
import kotlin.system.exitProcess

@Composable
fun HomeScreen(navController: NavController, viewModel: HomeViewModel) {
    val taskState by viewModel.taskState.collectAsState()
    val lastBackPressedTime = remember { mutableLongStateOf(0L) }
    val context = LocalContext.current
    val titleState = remember { mutableStateOf("") }
    val dueDateState = remember { mutableStateOf("") }
    val priorityState = remember { mutableStateOf("") }
    val showDatePicker = remember { mutableStateOf(false) }

    BackHandler {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastBackPressedTime.longValue < 2000) {
            exitProcess(0)
        } else {
            lastBackPressedTime.longValue = currentTime
            Toast.makeText(context, "Press back again to exit", Toast.LENGTH_SHORT).show()
        }
    }

    if (showDatePicker.value) {
        DatePickerDialog(
            onDateSelected = { selectedDate ->
                dueDateState.value = selectedDate
                showDatePicker.value = false
            },
            onDismiss = { showDatePicker.value = false }
        )
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.screen_bg),
            contentDescription = "Login Screen Background",
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 25.sdp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CustomButton(
                modifier = Modifier.padding(top = 20.sdp),
                text = "Log out",
                onClick = {
                    FirebaseAuth.getInstance().signOut()
                    Toast.makeText(context, "Logged out successfully", Toast.LENGTH_SHORT).show()
                    navController.navigate(Routes.loginScreen) {
                        popUpTo(Routes.homeScreen) { inclusive = true }
                    }
                }
            )

            CustomOutlinedTextField(
                value = titleState.value,
                onValueChange = { titleState.value = it },
                label = "Enter Title",
                visualTransformation = VisualTransformation.None
            )
            CustomOutlinedTextField(
                value = priorityState.value,
                onValueChange = { input ->
                    if (input.isEmpty() || input in listOf("1", "2", "3")) {
                        priorityState.value = input
                    }
                },
                label = "Enter Priority (1-3)",
                visualTransformation = VisualTransformation.None
            )
            CustomOutlinedTextField(
                value = dueDateState.value,
                onValueChange = { },
                isReadOnly = true,
                label = "Select date",
                trailingIcon = {
                    IconButton(
                        onClick = { showDatePicker.value = true }
                    ) {
                        Icon(
                            imageVector = Icons.Default.DateRange,
                            contentDescription = "Date",
                            tint = Color.Black
                        )
                    }
                },
                visualTransformation = VisualTransformation.None
            )
            Button(
                onClick = {
                    if (titleState.value.isNotEmpty() && dueDateState.value != "Select Date" && priorityState.value.isNotEmpty()) {
                        viewModel.taskIntent.trySend(
                            HomeIntent.AddTask(
                                Task(
                                    title = titleState.value,
                                    dueDate = System.currentTimeMillis(),
                                    priority = priorityState.value.toInt()
                                )
                            )
                        )
                        titleState.value = ""
                        dueDateState.value = ""
                        priorityState.value = ""
                    } else {
                        Toast.makeText(
                            context,
                            "Please fill all fields correctly",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Green
                )

            ) {
                Text(
                    text = "Add Task",
                    color = Color.White
                )
            }

            LazyColumn {
                when (taskState) {
                    is HomeState.Loading -> item { Text("Loading...") }
                    is HomeState.Success -> {
                        val tasks = (taskState as HomeState.Success).tasks
                        if (tasks.isEmpty()) {
                            item { Text("No tasks available") }
                        } else {
                            items(tasks) { task ->
                                TaskItem(task, viewModel)
                            }
                        }
                    }

                    is HomeState.Error -> item { Text("Error loading tasks") }
                }
            }
        }
    }
}

@Composable
fun TaskItem(task: Task, viewModel: HomeViewModel) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(text = task.title, modifier = Modifier.weight(1f))
        IconButton(onClick = { viewModel.taskIntent.trySend(HomeIntent.DeleteTask(task)) }) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Delete Task",
                tint = Color.Red
            )
        }
    }
}

