package com.test.newsapp.services

import com.test.newsapp.models.ArticlePage
import com.test.newsapp.network.Net
import okhttp3.Response

class NewsService {

    companion object {
        var instance = NewsService()
    }

    var breakignNews: ArticlePage = ArticlePage()
    var topNews: ArticlePage = ArticlePage()


    fun fetchNews() {
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