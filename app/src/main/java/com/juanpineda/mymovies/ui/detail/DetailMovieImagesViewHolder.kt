package com.juanpineda.mymovies.ui.detail

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.juanpineda.domain.MovieImage
import com.juanpineda.mymovies.data.server.BASE_URL_IMAGES_185
import com.juanpineda.mymovies.databinding.ViewImageBinding
import com.juanpineda.mymovies.ui.common.loadUrl

class DetailMovieImagesViewHolder(view: View) :
    RecyclerView.ViewHolder(view) {
    private val binding = ViewImageBinding.bind(view)
    fun bind(movieImage: MovieImage) {
        binding.imageViewPicture.loadUrl(BASE_URL_IMAGES_185 + movieImage.file_path)
    }
}