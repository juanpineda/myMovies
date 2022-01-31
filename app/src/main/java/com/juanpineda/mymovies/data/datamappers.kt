package com.juanpineda.mymovies.data

import com.juanpineda.domain.Movie
import com.juanpineda.mymovies.data.database.Movie as DomainMovie
import com.juanpineda.mymovies.data.server.Movie as ServerMovie
import com.juanpineda.domain.MovieImage
import com.juanpineda.mymovies.data.server.TheMoveDbImage

fun Movie.toRoomMovie(): DomainMovie =
    DomainMovie(
        id,
        title,
        overview,
        releaseDate,
        posterPath,
        backdropPath,
        originalLanguage,
        originalTitle,
        popularity,
        voteAverage,
        favorite,
        adult,
        myVote
    )

fun DomainMovie.toDomainMovie(): Movie = Movie(
    id,
    title,
    overview,
    releaseDate,
    posterPath,
    backdropPath,
    originalLanguage,
    originalTitle,
    popularity,
    voteAverage,
    favorite,
    adult,
    myVote
)

fun ServerMovie.toDomainMovie(): Movie =
    Movie(
        id,
        title,
        overview,
        releaseDate,
        posterPath,
        backdropPath ?: posterPath,
        originalLanguage,
        originalTitle,
        popularity,
        voteAverage,
        false,
        adult,
        0f
    )

fun TheMoveDbImage.toDomainMovie(): MovieImage =
    MovieImage(
        aspect_ratio,
        file_path,
        height,
        vote_average,
        vote_count,
        width
    )