package com.sample.newsapp.page

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.sample.newsapp.NewsViewModel
import java.text.SimpleDateFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArticlePage(
    articleUrl: String?,
    newsViewModel: NewsViewModel,
    navController: NavController  // Ganti onNavigateBack dengan NavController
) {
    val article by newsViewModel.getArticleById(articleUrl ?: "").observeAsState()
    val context = LocalContext.current

    article?.let { selectedArticle ->
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Article Details") },
                    navigationIcon = {
                        IconButton(onClick = {
                            navController.popBackStack()  // Kembali ke halaman sebelumnya
                        }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back"
                            )
                        }
                    }
                )
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
                // Rest of the existing code remains the same
                selectedArticle.urlToImage?.let { imageUrl ->
                    Image(
                        painter = rememberAsyncImagePainter(model = imageUrl),
                        contentDescription = "Article Image",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(250.dp),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }

                Text(
                    text = selectedArticle.title ?: "No Title",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Author: ${selectedArticle.author ?: "Unknown"}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(8.dp))

                val formattedDate = selectedArticle.publishedAt?.let {
                    try {
                        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
                        val date = inputFormat.parse(it)
                        val outputFormat = SimpleDateFormat("EEE, d MMM HH.mm", Locale("id", "ID"))
                        date?.let { parsedDate -> outputFormat.format(parsedDate) }
                    } catch (e: Exception) {
                        it
                    }
                } ?: "No Date"

                Text(
                    text = "Published: $formattedDate",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = selectedArticle.description ?: "No description available.",
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Read Full Article",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Blue,
                    modifier = Modifier.clickable {
                        selectedArticle.url?.let { url ->
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                            context.startActivity(intent)
                        }
                    }
                )
            }
        }
    } ?: run {
        Text("Article not found")
    }
}
