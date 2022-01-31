package com.juanpineda.mymovies.data.server

data class TheMovieDbImagesResult(
    val backdrops: List<TheMoveDbImage>,
    val id: Int,
    val posters: List<TheMoveDbImage>
)