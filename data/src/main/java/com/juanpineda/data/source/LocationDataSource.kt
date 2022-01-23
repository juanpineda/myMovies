package com.juanpineda.data.source

interface LocationDataSource {
    suspend fun findLastRegion(): String?
}