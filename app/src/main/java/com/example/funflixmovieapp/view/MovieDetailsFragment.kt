package com.example.funflixmovieapp.view

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.graphics.Color
import android.os.Build
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.funflixmovieapp.databinding.FragmentMovieDetailsBinding
import com.example.funflixmovieapp.utils.Constants.POSTER_BASE_URL
import com.example.funflixmovieapp.viewmodel.MovieDetailsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MovieDetailsFragment : Fragment() {

    private var _binding: FragmentMovieDetailsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MovieDetailsViewModel by viewModels()

    val TAG = "MovieDetailsFragment"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentMovieDetailsBinding.inflate(inflater, container, false)

        //Status Bar
        val window = requireActivity().window

        // Before Android 6.0 Marshmallow
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            // Status bar will be white
            window.statusBarColor = Color.WHITE
        } else {
            if (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_NO) {
                // Light mode
                window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                window.statusBarColor = Color.WHITE
            } else {
                // Dark mode
                window.decorView.systemUiVisibility = 0
                window.statusBarColor = Color.BLACK
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val movieId = MovieDetailsFragmentArgs.fromBundle(requireArguments()).movieId
        viewModel.getMovieDetails(movieId)

        observeLiveData()
    }

    private fun observeLiveData() {
        viewModel._movieDetails.observe(viewLifecycleOwner) { movieDetails ->
            // Update UI with movieDetails here
            movieDetails?.let {
                binding.tvMovieTitle.text = it.title
                binding.tvReleseDate.text = it.releaseDate
                binding.tvRate.text = String.format("%.1f/10", it.voteAverage)
                binding.tvMovieDetail.text = it.overview

                val moviePosterURL = POSTER_BASE_URL + it.posterPath
                Glide.with(this).load(moviePosterURL).into(binding.imgMovieDetailCover)
            }
        }

        viewModel._moviesLoading.observe(viewLifecycleOwner) { loading ->
            loading?.let {
                if (it) {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.scrollView.visibility = View.GONE
                } else {
                    binding.progressBar.visibility = View.GONE
                    binding.scrollView.visibility = View.VISIBLE
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}