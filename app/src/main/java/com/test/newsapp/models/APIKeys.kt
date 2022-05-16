package com.test.newsapp.models

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
class Article(
    @SerializedName("source")
    var source: Source = Source(),
    @SerializedName("author")
    var author: String = "",
    @SerializedName("title")
    var title: String = "",
    @SerializedName("description")
    var description: String = "",
    @SerializedName("url")
    var url: String = "",
    @SerializedName("urlToImage")
    var urlToImage: String = "",
    @SerializedName("publishedAt")
    var publishedAt: String = "",
    @SerializedName("content")
    var content: String = ""
)
