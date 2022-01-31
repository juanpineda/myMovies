package com.juanpineda.mymovies.data.server

import com.juanpineda.data.result.ResultHandler
import com.juanpineda.data.result.resultHandlerOf
import com.juanpineda.data.source.RemoteDataSource
import com.juanpineda.domain.Movie
import com.juanpineda.domain.MovieImage
import com.juanpineda.mymovies.data.toDomainMovie
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TheMovieDbDataSource(private val theMovieDb: TheMovieDb) : RemoteDataSource {

    override suspend fun getPopularMovies(
        apiKey: String,
        region: String
    ): ResultHandler<List<Movie>> =
        withContext(Dispatchers.IO) {
            resultHandlerOf {
                theMovieDb.service
                    .listPopularMoviesAsync(apiKey, region)
                    .results
                    .map { it.toDomainMovie() }
            }
        }

    override suspend fun getMovieImages(
        apiKey: String,
        movieId: Int
    ): List<MovieImage> =
        withContext(Dispatchers.IO) {
            theMovieDb.service
                .listMoviesImagesAsync(movieId, apiKey)
                .posters
                .map { it.toDomainMovie() }
        }
}