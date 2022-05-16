package com.test.newsapp.ui.fragments.home

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.test.newsapp.R
import com.test.newsapp.databinding.FragmentHomeBinding
import com.test.newsapp.models.Article
import com.test.newsapp.ui.fragments.register.ValidationEvent
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    companion object {
        fun newInstance() = HomeFragment()
    }

    private lateinit var viewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private var newsAdapter: NewsAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.events.collect { event: HomePageEvents ->
                when (event) {
                    is HomePageEvents.FetchHeadlinesSuccess -> {
                        setupAdapter()
                    }
                    is HomePageEvents.FetchHeadlinesError -> {
                        Snackbar.make(
                            requireView(),
                            "Something went wrong",
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
        viewModel.fetchNewsService()

    }


    private fun setupAdapter() {

        newsAdapter = NewsAdapter(viewModel.articlePage.articles).apply {
            onClickItems = { position: Int, article: Article, list: ArrayList<Article> ->

                Snackbar.make(
                    requireView(),
                    "Click on ${article.title}",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }
        binding.newsList.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = newsAdapter
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}