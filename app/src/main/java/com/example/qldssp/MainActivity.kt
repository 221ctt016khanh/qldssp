package com.example.qldssp

import LoginScreenG
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.qldssp.ui.theme.QLDSSPTheme
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.qldssp.*
import androidx.compose.foundation.layout.Box
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            QLDSSPTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                        navigationScreen()
                    }
                }
            }
        }
    }
}
@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun navigationScreen() {
   val navController=rememberNavController()
    NavHost(navController, startDestination = "loginScreen"){
        composable("loginScreen") {LoginScreenG(navController) }
        composable ("ListScreen"){ListScreen()}
        composable("AddScreen"){
            AddScreen()
        }
    }
}
