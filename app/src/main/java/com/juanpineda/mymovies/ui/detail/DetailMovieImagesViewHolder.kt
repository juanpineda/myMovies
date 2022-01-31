package com.juanpineda.mymovies.ui.detail

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.juanpineda.domain.MovieImage
import com.juanpineda.mymovies.databinding.ViewImageBinding
import com.juanpineda.mymovies.ui.common.loadUrl

class DetailMovieImagesViewHolder(view: View) :
    RecyclerView.ViewHolder(view) {
    private val binding = ViewImageBinding.bind(view)
    fun bind(movieImage: MovieImage) {
        binding.imageViewPicture.loadUrl("https://image.tmdb.org/t/p/w185/${movieImage.file_path}")
    }
}