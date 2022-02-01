package com.juanpineda.mymovies.ui.detail

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.juanpineda.domain.MovieImage
import com.juanpineda.mymovies.R
import com.juanpineda.mymovies.data.server.BASE_URL_IMAGES_185
import com.juanpineda.mymovies.databinding.ViewImageBinding
import com.juanpineda.mymovies.ui.common.inflate
import com.juanpineda.mymovies.ui.common.loadUrl

class DetailMovieImagesAdapter(private val pictures: List<MovieImage>) :
    RecyclerView.Adapter<DetailMovieImagesAdapter.DetailMovieImagesViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailMovieImagesViewHolder {
        val view = parent.inflate(R.layout.view_image, false)
        return DetailMovieImagesViewHolder(view)
    }

    override fun onBindViewHolder(holder: DetailMovieImagesViewHolder, position: Int) {
        holder.bind(pictures[position])
    }

    override fun getItemCount(): Int = pictures.size

    class DetailMovieImagesViewHolder(view: View) :
        RecyclerView.ViewHolder(view) {
        private val binding = ViewImageBinding.bind(view)
        fun bind(movieImage: MovieImage) {
            binding.imageViewPicture.loadUrl(BASE_URL_IMAGES_185 + movieImage.file_path)
        }
    }
}