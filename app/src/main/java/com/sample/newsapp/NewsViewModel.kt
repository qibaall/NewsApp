package com.sample.newsapp

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kwabenaberko.newsapilib.NewsApiClient
import com.kwabenaberko.newsapilib.models.Article
import com.kwabenaberko.newsapilib.models.request.TopHeadlinesRequest
import com.kwabenaberko.newsapilib.models.request.EverythingRequest
import com.kwabenaberko.newsapilib.models.response.ArticleResponse

class NewsViewModel : ViewModel() {

    private val _articles = MutableLiveData<List<Article>>()
    val articles: LiveData<List<Article>> = _articles

    init {
        fetchNewsTopHeadlines()
    }

    // Fetch top headlines
    private fun fetchNewsTopHeadlines() {
        val newsApiClient = NewsApiClient(Constant.apiKey)

        val request = TopHeadlinesRequest.Builder()
            .language("en")
            .build()

        newsApiClient.getTopHeadlines(request, object : NewsApiClient.ArticlesResponseCallback {
            override fun onSuccess(response: ArticleResponse?) {
                response?.articles?.let {
                    // Post value on the main thread
                    _articles.postValue(it)
                }
            }

            override fun onFailure(throwable: Throwable?) {
                throwable?.localizedMessage?.let {
                    Log.e("NewsAPI Response Failed", it)
                }
            }
        })
    }

    // Fetch articles based on a search query
    fun fetchEverythingWithQuery(query: String) {
        val newsApiClient = NewsApiClient(Constant.apiKey)

        val request = EverythingRequest.Builder()
            .language("en")
            .q(query) // Modify the query to search for specific terms
            .build()

        newsApiClient.getEverything(request, object : NewsApiClient.ArticlesResponseCallback {
            override fun onSuccess(response: ArticleResponse?) {
                response?.articles?.let {
                    // Post value on the main thread
                    _articles.postValue(it)
                }
            }

            override fun onFailure(throwable: Throwable?) {
                throwable?.localizedMessage?.let {
                    Log.e("NewsAPI Response Failed", it)
                }
            }
        })
    }
    fun getArticleById(articleUrl: String): LiveData<Article> {
        return MutableLiveData(
            articles.value?.find { it.url == articleUrl }
        )
    }
}
