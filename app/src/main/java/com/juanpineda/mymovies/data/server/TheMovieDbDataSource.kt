package com.juanpineda.mymovies.data.server

import com.juanpineda.data.result.resultHandlerOf
import com.juanpineda.data.source.RemoteDataSource
import com.juanpineda.mymovies.data.toDomainMovie
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TheMovieDbDataSource(private val theMovieDb: TheMovieDb) : RemoteDataSource {

    override suspend fun getPopularMovies(
        apiKey: String,
        region: String
    ) = withContext(Dispatchers.IO) {
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
    ) = withContext(Dispatchers.IO) {
        resultHandlerOf {
            theMovieDb.service
                .listMoviesImagesAsync(movieId, apiKey)
                .posters
                .map { it.toDomainMovie() }
        }
    }
}