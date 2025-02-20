package com.sample.newsapp.page

import android.content.Context
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.kwabenaberko.newsapilib.models.Article
import com.sample.newsapp.pref.ArticlePreferences

@Composable
fun ReadArticlesScreen(
    navController: NavHostController,
    context: Context
) {
    // Get the list of read articles from SharedPreferences
    val articlePreferences = ArticlePreferences(context)
    val readArticles = articlePreferences.getReadArticles()

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(readArticles) { article ->
            ReadArticleItem(
                article = article,
                onClick = {
                    article.url?.let { url ->
                        navController.navigate("articleDetail/${Uri.encode(url)}")
                    }
                }
            )
        }
    }
}

@Composable
fun ReadArticleItem(
    article: Article,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .padding(12.dp)
            .fillMaxWidth()
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            article.urlToImage?.let { imageUrl ->
                Image(
                    painter = rememberAsyncImagePainter(imageUrl),
                    contentDescription = "Article Image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp)
                )
            }
            Text(
                text = article.title ?: "No Title",
                modifier = Modifier.padding(top = 12.dp),
                maxLines = 2,
                style = TextStyle(fontSize = 18.sp)
            )
        }
    }
}