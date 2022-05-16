package com.test.newsapp.models

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
class ArticlePage(
    @SerializedName("status")
    var status: String = "",
    @SerializedName("totalResults")
    var totalResults: Int = 0,
    @SerializedName("articles")
    var articles: ArrayList<Article> = arrayListOf(),
) {

    fun getNextPageNumber(): Int {
        if (!hasNextPage()) return -1
        return (articles.size / 100) + 1
    }

    fun hasNextPage(): Boolean {

        return totalResults > articles.size
    }

}