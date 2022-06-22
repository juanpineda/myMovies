package com.juanpineda.mymovies.ui.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.juanpineda.domain.Movie
import com.juanpineda.mymovies.R
import com.juanpineda.mymovies.data.server.BASE_URL_IMAGES_185

@Composable
fun MovieItemView(movie: Movie, modifier: Modifier = Modifier) {
    Card(
        backgroundColor = MaterialTheme.colors.primaryVariant,
        modifier = modifier.padding(2.dp)
    ) {
        Column {
            Image(
                painter = rememberImagePainter(BASE_URL_IMAGES_185 + movie.posterPath),
                contentDescription = movie.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxHeight()
                    .aspectRatio(1 / 1.5f)
            )
            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .padding(8.dp, 16.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = movie.title,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.body1,
                    modifier = Modifier.padding(bottom = 10.dp).align(CenterHorizontally)
                )
                Image(
                    painter = painterResource(if (movie.favorite) R.drawable.ic_favorite_on else R.drawable.ic_favorite_off),
                    contentDescription = movie.title,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(30.dp)
                )
            }
        }
    }
}