package com.test.newsapp.ui.fragments.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.test.newsapp.R
import com.test.newsapp.databinding.NewsCardBinding
import com.test.newsapp.models.Article

class NewsAdapter(var list: ArrayList<Article>) :
    RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {

    var onClickItems: ((position: Int, article: Article, list: ArrayList<Article>) -> Unit)? = null

    class NewsViewHolder(binding: NewsCardBinding) : RecyclerView.ViewHolder(binding.root) {
        var title = binding.newsTitle
        var author = binding.author
        var description = binding.description
        var card = binding.card
        var poster = binding.poster

        fun setData(article: Article) {

            title.text = article.title
            author.text = ""
            article.author?.let {
                author.text = "by $it"
            }
            description.text = article.description

            Glide.with(poster)
                .load(article.urlToImage)
                .centerCrop()
                .placeholder(R.drawable.placeholder)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(poster)
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val binding = NewsCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NewsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        holder.setData(list.get(position))
        holder.card.setOnClickListener {
            onClickItems?.let {
                it(position, list.get(position), list)
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }


}