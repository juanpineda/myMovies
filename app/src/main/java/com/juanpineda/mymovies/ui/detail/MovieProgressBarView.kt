package com.juanpineda.mymovies.ui.detail

import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.juanpineda.domain.Movie
import com.juanpineda.mymovies.R

class MovieProgressBarView : ConstraintLayout {

    private lateinit var progressBar: ProgressBar
    private lateinit var textViewProgressValue: TextView

    constructor(context: Context) : super(context) {
        init(context, null)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        View.inflate(context, R.layout.view_progress_bar, this)
        progressBar = findViewById(R.id.progressBarMovies)
        textViewProgressValue = findViewById(R.id.textViewProgressValue)
    }

    fun setMovie(movie: Movie) = with((movie.voteAverage * 10).toInt()) {
        progressBar.progress = this
        textViewProgressValue.text = this.toString()
        progressBar.progressTintList =
            ColorStateList.valueOf(ContextCompat.getColor(context, getProgressColor(this)))
    }

    private fun getProgressColor(value: Int) =
        when (value) {
            in 0..45 -> R.color.colorAccent
            in 45..70 -> R.color.uik_yellow
            in 70..100 -> R.color.uik_green
            else -> R.color.colorAccent
        }
}