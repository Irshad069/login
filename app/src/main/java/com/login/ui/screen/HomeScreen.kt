package com.login.ui.screen

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.login.R
import com.login.navigation.Routes
import com.login.ui.screen.components.CustomButton
import com.login.ui.screen.login.LoginIntent
import network.chaintech.sdpcomposemultiplatform.sdp
import network.chaintech.sdpcomposemultiplatform.ssp
import kotlin.system.exitProcess

@Composable
fun HomeScreen(navController: NavController) {
    val lastBackPressedTime = remember { mutableLongStateOf(0L) }
    val context = LocalContext.current
    BackHandler {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastBackPressedTime.longValue < 2000) {
            exitProcess(0)
        } else {
            lastBackPressedTime.longValue = currentTime
            Toast.makeText(context, "Press back again to exit", Toast.LENGTH_SHORT).show()
        }
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
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Welcome to the world of work! Wishing you success, growth, and endless opportunities ahead.",
                textAlign = TextAlign.Center,
                color = Color.Magenta,
                fontSize = 16.ssp
            )
            CustomButton(
                modifier = Modifier.padding(top = 58.sdp),
                text = "Log out",
                onClick = {
                    FirebaseAuth.getInstance().signOut()
                    Toast.makeText(context, "Logged out successfully", Toast.LENGTH_SHORT).show()
                    navController.navigate(Routes.loginScreen) {
                        popUpTo(Routes.homeScreen) { inclusive = true }
                    }
                }
            )
        }
    }
}

@Preview
@Composable
private fun HomeScreenPreview() {
    HomeScreen(navController = rememberNavController())
}