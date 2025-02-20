package com.sample.newsapp.page

import android.content.Context
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.kwabenaberko.newsapilib.models.Article
import com.sample.newsapp.NewsViewModel
import com.sample.newsapp.pref.ArticlePreferences
import com.sample.newsapp.setting.SkeletonLoadingItem

@Composable
fun HomePage(
    newsViewModel: NewsViewModel,
    navController: NavHostController,
    context: Context
) {
    // State to hold the search query
    var searchQuery by remember { mutableStateOf("") }

    // Observing the articles list from the ViewModel
    val articles by newsViewModel.articles.observeAsState(emptyList())

    val articlePreferences = ArticlePreferences(context)

    // Filter the articles based on the search query
    val filteredArticles = articles.filter {
        it.title?.contains(searchQuery, ignoreCase = true) ?: false
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        // Search Bar
        SearchBar(
            query = searchQuery,
            onQueryChange = { searchQuery = it },
            onSearch = {
                if (searchQuery.isNotEmpty()) {
                    newsViewModel.fetchEverythingWithQuery(searchQuery)
                }
            }
        )

        if (filteredArticles.isEmpty()) {
            LazyColumn {
                items(5) {
                    SkeletonLoadingItem()
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                val patternCount = (filteredArticles.size + 3) / 4

                items(patternCount) { patternIndex ->
                    val largeItemIndex = patternIndex * 4
                    if (largeItemIndex < filteredArticles.size) {
                        LargeNewsItem(
                            article = filteredArticles[largeItemIndex],
                            articlePreferences = articlePreferences,
                            onClick = {
                                filteredArticles[largeItemIndex].url?.let { url ->
                                    navController.navigate("articleDetail/${Uri.encode(url)}")
                                }
                            }
                        )
                    }

                    val smallItemStartIndex = largeItemIndex + 1
                    if (smallItemStartIndex < filteredArticles.size) {
                        LazyRow(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            items(2) { rowIndex ->
                                val smallItemIndex = smallItemStartIndex + rowIndex
                                if (smallItemIndex < filteredArticles.size) {
                                    SmallNewsItem(
                                        article = filteredArticles[smallItemIndex],
                                        articlePreferences = articlePreferences,
                                        onClick = {
                                            filteredArticles[smallItemIndex].url?.let { url ->
                                                navController.navigate("articleDetail/${Uri.encode(url)}")
                                            }
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        Button(
            onClick = { navController.navigate("readArticles") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        ) {
            Text("View Read Articles")
        }
    }
}

@Composable
fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = query,
                onValueChange = onQueryChange,
                label = { Text("Search") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                trailingIcon = {
                    IconButton(onClick = onSearch) {
                        Icon(imageVector = Icons.Filled.Search, contentDescription = "Search Icon")
                    }
                },
                shape = RoundedCornerShape(12.dp)
            )
        }
    }
}

@Composable
fun LargeNewsItem(
    article: Article,
    articlePreferences: ArticlePreferences,
    onClick: () -> Unit
) {
    // Save the article as read when clicked
    val readArticles = articlePreferences.getReadArticles().toMutableList()
    if (!readArticles.contains(article)) {
        readArticles.add(article)
        articlePreferences.saveReadArticles(readArticles)
    }

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

@Composable
fun SmallNewsItem(
    article: Article,
    articlePreferences: ArticlePreferences,
    onClick: () -> Unit
) {
    val readArticles = articlePreferences.getReadArticles().toMutableList()
    if (!readArticles.contains(article)) {
        readArticles.add(article)
        articlePreferences.saveReadArticles(readArticles)
    }

    Card(
        modifier = Modifier
            .width(195.dp)
            .padding(8.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            article.urlToImage?.let { imageUrl ->
                Image(
                    painter = rememberAsyncImagePainter(imageUrl),
                    contentDescription = "Article Image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                )
            }
            Text(
                text = article.title ?: "No Title",
                modifier = Modifier.padding(top = 8.dp),
                maxLines = 2,
                style = TextStyle(fontSize = 14.sp)
            )
        }
    }
}