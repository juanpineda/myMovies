package com.juanpineda.mymovies.data.server

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TheMovieDbService {
    @GET("discover/movie?sort_by=popularity.desc")
    suspend fun listPopularMoviesAsync(
        @Query("api_key") apiKey: String,
        @Query("region") region: String
    ): MovieDbResult

    @GET("movie/{movieId}/images")
    suspend fun listMoviesImagesAsync(
        @Path("movieId") movieId: Int,
        @Query("api_key") apiKey: String
    ): TheMovieDbImagesResult
}