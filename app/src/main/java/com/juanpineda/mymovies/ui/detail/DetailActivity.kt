package com.juanpineda.mymovies.ui.detail

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.juanpineda.mymovies.R
import com.juanpineda.mymovies.databinding.ActivityDetailBinding
import com.juanpineda.mymovies.ui.common.loadContent
import com.juanpineda.mymovies.ui.common.loadUrl
import org.koin.androidx.scope.ScopeActivity
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class DetailActivity : ScopeActivity() {

    companion object {
        const val MOVIE = "DetailActivity:movie"
    }

    private val viewModel: DetailViewModel by viewModel {
        parametersOf(intent.getIntExtra(MOVIE, -1))
    }

    private lateinit var binding: ActivityDetailBinding

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.model.observe(this, Observer(::updateUi))

        binding.movieDetailFavorite.setOnClickListener { viewModel.onFavoriteClicked() }

        binding.ratingBar.setOnRatingBarChangeListener { _, value, _ ->
            viewModel.rateMovie(value)
        }
    }

    private fun updateUi(model: DetailViewModel.UiModel) = with(binding) {
        model.movie.run {
            movieDetailToolbar.title = title
            movieDetailImage.loadUrl("https://image.tmdb.org/t/p/w780${backdropPath}")
            movieDetailSummary.text = overview
            movieDetailInfo.setMovie(this)
            val icon = if (favorite) R.drawable.ic_favorite_on else R.drawable.ic_favorite_off
            movieDetailFavorite.setImageDrawable(
                ContextCompat.getDrawable(
                    this@DetailActivity,
                    icon
                )
            )
            progressBar.setMovie(this)
            binding.ratingBar.rating = myVote
        }
        if (model.movieImages.isNotEmpty())
            viewPagerMovieImages.loadContent(model.movieImages)
    }
}