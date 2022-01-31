package com.juanpineda.domain

data class MovieImage(
    val aspect_ratio: Double,
    val file_path: String,
    val height: Int,
    val vote_average: Double,
    val vote_count: Int,
    val width: Int
)