package com.example.funflixmovieapp.viewmodel

import android.app.Application
import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.funflixmovieapp.model.MovieDetails
import com.example.funflixmovieapp.model.UpcomingMovie
import com.example.funflixmovieapp.repository.ApiRepository
import com.example.funflixmovieapp.utils.Constants.POSTER_BASE_URL
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailsViewModel
@Inject constructor(
    private val apiRepository: ApiRepository, application: Application
) : BaseViewModel(application) {

    private val moviesLoading = MutableLiveData<Boolean>()
    val _moviesLoading: LiveData<Boolean>
        get() = moviesLoading

    private val movieDetails = MutableLiveData<MovieDetails>()
    val _movieDetails: LiveData<MovieDetails>
        get() = movieDetails

    fun getMovieDetails(movieId: Int) = launch {
        moviesLoading.value = true
        val result = apiRepository.getMovieDetails(movieId)
        result.body().let {
            if (result.isSuccessful) {
                moviesLoading.value = false
                movieDetails.postValue(it)
            } else {
                moviesLoading.value = false
                Log.e(TAG, "Error fetching movie details")
            }
        }

    }
}