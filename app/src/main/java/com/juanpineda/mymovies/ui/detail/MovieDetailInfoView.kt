package com.juanpineda.mymovies.ui.detail

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.text.bold
import androidx.core.text.buildSpannedString
import com.juanpineda.domain.Movie
import com.juanpineda.mymovies.R

class MovieDetailInfoView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr) {

    fun setMovie(movie: Movie) = with(movie) {
        text = buildSpannedString {

            bold { append(context.getString(R.string.movie_detail_info_view_item_1)) }
            appendLine(originalLanguage)

            bold { append(context.getString(R.string.movie_detail_info_view_item_2)) }
            appendLine(originalTitle)

            bold { append(context.getString(R.string.movie_detail_info_view_item_3)) }
            appendLine(releaseDate)

            bold { append(context.getString(R.string.movie_detail_info_view_item_4)) }
            appendLine(popularity.toString())

            bold { append(context.getString(R.string.movie_detail_info_view_item_5)) }
            append(
                if (adult) context.getString(R.string.movie_detail_info_view_item_5_yes)
                else context.getString(R.string.movie_detail_info_view_item_5_no)
            )
        }
    }
}