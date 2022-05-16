package com.test.newsapp.ui.fragments.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.test.newsapp.models.ArticlePage
import com.test.newsapp.services.NewsService
import com.test.newsapp.ui.fragments.register.ValidationEvent
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    val newsService: NewsService = NewsService.instance
) : ViewModel() {

    private val eventChannel = Channel<HomePageEvents>()
    val events = eventChannel.receiveAsFlow()
    var articlePage: ArticlePage = ArticlePage()


    fun fetchNewsService() {
        newsService.fetchHeadlines(
            success = {
                articlePage = it
                viewModelScope.launch {
                    eventChannel.send(HomePageEvents.FetchHeadlinesSuccess)
                }
            },
            error = {
                viewModelScope.launch {
                    eventChannel.send(
                        HomePageEvents.FetchHeadlinesError(
                            code = it.code,
                            message = "Something went Wrong"
                        )
                    )
                }
            }
        )
    }

}

sealed class HomePageEvents {
    object FetchHeadlinesSuccess : HomePageEvents()
    data class FetchHeadlinesError(val code: Int, val message: String) : HomePageEvents()
}
