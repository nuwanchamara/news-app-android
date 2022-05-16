package com.test.newsapp.services

import android.util.Log
import com.google.gson.Gson
import com.test.newsapp.constants.APIKeys
import com.test.newsapp.models.ArticlePage
import com.test.newsapp.network.Net
import com.test.newsapp.network.NetException
import okhttp3.Response

class NewsService {

    companion object {
        var instance = NewsService()
    }

    var logTag = "NewsService"
    var breakignNews: ArticlePage = ArticlePage()
    var topHeadlines: ArticlePage = ArticlePage()
    val gson: Gson = Gson()

    fun fetchHeadlines(
        success: ((topHeadlines: ArticlePage) -> Unit),
        error: ((exception: NetException) -> Unit)
    ) {

        val queryParams: HashMap<String, Any> = hashMapOf()
        queryParams["apiKey"] = APIKeys.API_KEY
        queryParams["country"] = "us"


        val net = Net(
            url = Net.URL.TOP_HEADLINES,
            queryParam = queryParams,
            headers = hashMapOf(),
            method = Net.NetMethod.GET,
        )
        net.perform(
            success = { result: String, okHttpResponse: Response? ->
                Log.d(logTag,"news fetch success")
                topHeadlines = gson.fromJson(result, ArticlePage::class.java)
                success(topHeadlines)

            },
            error = {
                error(it)
            }
        )
    }


    fun fetchBreakingNews() {
        val net = Net(
            url = Net.URL.SEARCH_NEWS,
            queryParam = hashMapOf(),
            headers = hashMapOf(),
            method = Net.NetMethod.GET,
        )
        net.perform(
            success = { result: String, okHttpResponse: Response? -> },
            error = {

            }
        )
    }

    fun fetchBreakingNewsNextPage() {
        if (breakignNews.hasNextPage()) {

            return
        }

        val net = Net(
            url = Net.URL.SEARCH_NEWS,
            queryParam = hashMapOf(),
            headers = hashMapOf(),
            method = Net.NetMethod.GET,
        )
        net.perform(
            success = { result: String, okHttpResponse: Response? -> },
            error = {

            }
        )
    }

}