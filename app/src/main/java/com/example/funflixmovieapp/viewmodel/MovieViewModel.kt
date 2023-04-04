package com.example.funflixmovieapp.viewmodel

import android.app.Application
import android.media.Image
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bumptech.glide.load.engine.Resource
import com.example.funflixmovieapp.model.NowPlaying
import com.example.funflixmovieapp.model.UpcomingMovie
import com.example.funflixmovieapp.repository.ApiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieViewModel
@Inject constructor(
    private val apiRepository: ApiRepository, application: Application
) : BaseViewModel(application) {

    private val response = MutableLiveData<List<UpcomingMovie>>()
    val _response: LiveData<List<UpcomingMovie>>
        get() = response

    private val moviesError = MutableLiveData<Boolean>()
    val _moviesError: LiveData<Boolean>
        get() = moviesError

    private val moviesLoading = MutableLiveData<Boolean>()
    val _moviesLoading: LiveData<Boolean>
        get() = moviesLoading

    private val _nowPlayingMovies = MutableLiveData<List<NowPlaying.Result>>()
    val nowPlayingMovies: LiveData<List<NowPlaying.Result>>
        get() = _nowPlayingMovies

    fun getMovies() {
        moviesLoading.value = true

        launch {
            val upcomingMoviesResponse = apiRepository.getUpcomingMoviesList(1)
            if (upcomingMoviesResponse.isSuccessful) {
                upcomingMoviesResponse.body()!!.results.let {
                    moviesError.value = false
                    moviesLoading.value = false
                    response.postValue(it)
                }
            } else {
                moviesError.value = true
                moviesLoading.value = false
            }
        }
    }

    fun refreshData() {
        getMovies()
    }
}