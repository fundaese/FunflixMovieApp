package com.example.funflixmovieapp.model

import com.google.gson.annotations.SerializedName

data class UpcomingMovieList(
    @SerializedName("page")
    val page: Int,
    @SerializedName("results")
    val results: List<UpcomingMovie>,
)