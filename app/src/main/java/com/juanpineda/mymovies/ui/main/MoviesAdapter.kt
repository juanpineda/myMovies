package com.juanpineda.mymovies.ui.main

import android.view.ViewGroup
import androidx.compose.foundation.clickable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.composethemeadapter.MdcTheme
import com.juanpineda.domain.Movie
import com.juanpineda.mymovies.ui.common.basicDiffUtil

class MoviesAdapter(private val listener: (Movie) -> Unit) :
    RecyclerView.Adapter<MoviesAdapter.ViewHolder>() {

    var movies: List<Movie> by basicDiffUtil(
        emptyList(),
        areItemsTheSame = { old, new -> old.id == new.id }
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ComposeView(parent.context), listener)
    }

    override fun getItemCount(): Int = movies.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val movie = movies[position]
        holder.bind(movie)
        holder.itemView.setOnClickListener { listener(movie) }
    }

    override fun onViewRecycled(holder: ViewHolder) {
        holder.composeView.disposeComposition()
    }

    class ViewHolder(
        val composeView: ComposeView,
        private val listener: (Movie) -> Unit
    ) :
        RecyclerView.ViewHolder(composeView) {

        init {
            composeView.setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
        }

        fun bind(movie: Movie) {
            composeView.setContent {
                MdcTheme{
                    MovieItemView(movie = movie, Modifier.clickable { listener(movie) })
                }
            }
        }
    }
}