package com.example.funflixmovieapp.api

import com.example.funflixmovieapp.model.MovieDetails
import com.example.funflixmovieapp.model.NowPlaying
import com.example.funflixmovieapp.model.UpcomingMovieList
import com.example.funflixmovieapp.utils.Constants.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("movie/upcoming?api_key=$API_KEY")
    suspend fun getUpcomingMoviesList(@Query("page") page: Int): Response<UpcomingMovieList>

    @GET("movie/{movie_id}?api_key=$API_KEY")
    suspend fun getMovieDetails(@Path("movie_id") id: Int): Response<MovieDetails>
}