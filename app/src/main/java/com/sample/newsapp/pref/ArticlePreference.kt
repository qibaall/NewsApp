package com.sample.newsapp.pref

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.kwabenaberko.newsapilib.models.Article

class ArticlePreferences(context: Context) {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("ReadArticles", Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = sharedPreferences.edit()

    // Convert list of Articles to JSON and save it
    fun saveReadArticles(articles: List<Article>) {
        val gson = Gson()
        val json = gson.toJson(articles)
        editor.putString("read_articles", json)
        editor.apply()
    }

    // Retrieve the list of read articles
    fun getReadArticles(): List<Article> {
        val gson = Gson()
        val json = sharedPreferences.getString("read_articles", null)
        val type = object : TypeToken<List<Article>>() {}.type
        return if (json != null) {
            gson.fromJson(json, type)
        } else {
            emptyList()
        }
    }
}
