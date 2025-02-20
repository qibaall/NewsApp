package com.sample.newsapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.sample.newsapp.page.ArticlePage
import com.sample.newsapp.page.HomePage
import com.sample.newsapp.page.ReadArticlesScreen
import com.sample.newsapp.ui.theme.NewsAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Initialize the ViewModel
        val newsViewModel = ViewModelProvider(this)[NewsViewModel::class.java]

        setContent {
            NewsAppTheme {
                // Use a NavController for navigation
                val navController = rememberNavController()

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize()
                    ) {
                        // App Title
                        Text(
                            text = "NewsAPP",
                            modifier = Modifier.align(Alignment.CenterHorizontally),
                            color = Color.Blue,
                            fontSize = 25.sp,
                            fontFamily = FontFamily.Serif
                        )

                        // Navigation Host
                        NavHost(
                            navController = navController,
                            startDestination = "home",


                        ) {
                            // Home Screen
                            composable("home") {
                                HomePage(
                                    newsViewModel = newsViewModel,
                                    navController = navController,
                                    context = this@MainActivity
                                )
                            }

                            // Article Detail Screen
                            composable("articleDetail/{articleUrl}") { backStackEntry ->
                                val articleUrl = backStackEntry.arguments?.getString("articleUrl")
                                ArticlePage(
                                    articleUrl = articleUrl,
                                    newsViewModel = newsViewModel,
                                    navController = navController
                                )
                            }
                            composable("readArticles") {
                                ReadArticlesScreen(navController = navController, context = this@MainActivity)
                            }
                        }
                    }
                }
            }
        }
    }
}