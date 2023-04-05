package com.example.funflixmovieapp.repository

import com.example.funflixmovieapp.api.ApiService
import javax.inject.Inject

class ApiRepository @Inject constructor(
    private val apiService: ApiService,
) {
    suspend fun getUpcomingMoviesList(page: Int) = apiService.getUpcomingMoviesList(page)
    suspend fun getMovieDetails(id: Int) = apiService.getMovieDetails(id)
    suspend fun getNowPlayingList(page: Int) = apiService.getNowPlayingMovies(page)
}