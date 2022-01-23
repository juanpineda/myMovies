package com.juanpineda.mymovies.data.server

import com.juanpineda.data.source.RemoteDataSource
import com.juanpineda.domain.Movie
import com.juanpineda.mymovies.data.toDomainMovie

class TheMovieDbDataSource(private val theMovieDb: TheMovieDb) : RemoteDataSource {

    override suspend fun getPopularMovies(apiKey: String, region: String): List<Movie> =
        theMovieDb.service
            .listPopularMoviesAsync(apiKey, region)
            .results
            .map { it.toDomainMovie() }
}