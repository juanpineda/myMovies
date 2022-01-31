package com.juanpineda.mymovies.ui.detail

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.juanpineda.domain.MovieImage
import com.juanpineda.mymovies.R
import com.juanpineda.mymovies.ui.common.inflate

class DetailMovieImagesAdapter(private val pictures: List<MovieImage>) :
    RecyclerView.Adapter<DetailMovieImagesViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailMovieImagesViewHolder {
        val view = parent.inflate(R.layout.view_image, false)
        return DetailMovieImagesViewHolder(view)
    }

    override fun onBindViewHolder(holder: DetailMovieImagesViewHolder, position: Int) {
        holder.bind(pictures[position])
    }

    override fun getItemCount(): Int = pictures.size
}