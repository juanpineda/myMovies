package com.juanpineda.mymovies.ui.detail

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.RatingBar
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.snackbar.Snackbar.LENGTH_SHORT
import com.juanpineda.domain.Movie
import com.juanpineda.domain.MovieImage
import com.juanpineda.mymovies.R
import com.juanpineda.mymovies.databinding.ActivityDetailBinding
import com.juanpineda.mymovies.ui.common.applyVote
import com.juanpineda.mymovies.ui.common.loadContent
import com.juanpineda.mymovies.ui.common.loadUrl
import com.juanpineda.mymovies.ui.detail.DetailViewModel.UiModel
import com.juanpineda.mymovies.ui.detail.VoteDialogFragment.Companion.TAG
import org.koin.androidx.scope.ScopeActivity
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class DetailActivity : ScopeActivity(), RatingBar.OnRatingBarChangeListener {

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

        binding.ratingBar.onRatingBarChangeListener = this
    }

    private fun updateUi(model: UiModel) = when (model) {
        is UiModel.ContentMovie -> loadMovieView(model.movie)
        is UiModel.ContentMovieImages -> loadMovieImagesView(model.movieImages)
        is UiModel.ContentMovieFavorite -> loadFavoriteView(model.movie)
    }

    private fun loadMovieView(movie: Movie) = with(binding) {
        movieDetailToolbar.title = title
        movieDetailImage.loadUrl("https://image.tmdb.org/t/p/w780${movie.backdropPath}")
        movieDetailSummary.text = movie.overview
        movieDetailInfo.setMovie(movie)
        loadFavoriteView(movie, false)
        progressBar.setMovie(movie)
        ratingBar.applyVote(this@DetailActivity, movie.myVote)
        ratingBar.setIsIndicator(movie.myVote != 0f)
        if (ratingBar.isIndicator)
            ratingBar.progressTintList = ColorStateList.valueOf(
                ContextCompat.getColor(
                    this@DetailActivity,
                    R.color.colorPrimary
                )
            )
    }

    private fun loadMovieImagesView(movieImages: List<MovieImage>) = with(binding) {
        if (movieImages.isNotEmpty()) {
            viewPagerMovieImages.visibility = VISIBLE
            viewPagerMovieImages.loadContent(movieImages)
        } else viewPagerMovieImages.visibility = GONE
    }

    private fun loadFavoriteView(movie: Movie, showSnackBar: Boolean = true) = with(movie) {
        val icon = if (favorite) R.drawable.ic_favorite_on else R.drawable.ic_favorite_off
        binding.movieDetailFavorite.setImageDrawable(
            ContextCompat.getDrawable(
                this@DetailActivity,
                icon
            )
        )
        if (showSnackBar && favorite)
            Snackbar.make(
                findViewById(android.R.id.content),
                getString(R.string.detail_movie_add_to_favorite),
                LENGTH_SHORT
            ).show()
    }

    private fun showVoteDialogFragment(value: Float) =
        VoteDialogFragment.Builder()
            .setVote(value)
            .setOnVoteClickListener(viewModel::rateMovie)
            .setOnRankingBarChangeListener {
                binding.ratingBar.applyVote(this@DetailActivity, it)
            }.create()
            .show(this.supportFragmentManager, TAG)

    override fun onRatingChanged(p0: RatingBar?, value: Float, p2: Boolean) =
        showVoteDialogFragment(value)
}