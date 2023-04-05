package com.example.funflixmovieapp.view

import android.content.res.Configuration
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.funflixmovieapp.adapter.ImageSliderAdapter
import com.example.funflixmovieapp.adapter.MovieAdapter
import com.example.funflixmovieapp.databinding.FragmentMovieBinding
import com.example.funflixmovieapp.model.NowPlaying
import com.example.funflixmovieapp.viewmodel.MovieViewModel
import dagger.hilt.android.AndroidEntryPoint
import me.relex.circleindicator.CircleIndicator

@AndroidEntryPoint
class MovieFragment : Fragment() {

    private var _binding: FragmentMovieBinding? = null
    private val binding get() = _binding!!

    private lateinit var movieAdapter: MovieAdapter
    private lateinit var adapter: ImageSliderAdapter

    private val viewModel: MovieViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentMovieBinding.inflate(inflater, container, false)

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

        viewModel.refreshData()

        binding.rvMovieList.layoutManager = LinearLayoutManager(context)
        binding.swipeRefreshLayout.setOnRefreshListener {
            binding.rvMovieList.visibility = View.GONE
            binding.tvError.visibility = View.GONE
            binding.progressBar.visibility = View.VISIBLE

            viewModel.refreshData()
            binding.swipeRefreshLayout.isRefreshing = false
        }

        observeLiveData()
        setupRecyclerView()
        setupViewPager()
    }

    private fun setupRecyclerView() {
        movieAdapter = MovieAdapter()

        binding.rvMovieList.apply {
            adapter = movieAdapter
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(
                DividerItemDecoration(
                    context,
                    DividerItemDecoration.VERTICAL
                )
            )
        }

        viewModel.getMovies()
        movieAdapter.setOnItemClickListener {
            val directions = MovieFragmentDirections.actionMovieFragmentToMovieDetailsFragment(it.id!!)
            findNavController().navigate(directions)
        }
    }

    private fun setupViewPager() {
        viewModel.fetchNowPlayingMovies()
        adapter = ImageSliderAdapter(requireContext(), emptyList())


        adapter.setOnItemClickListener {
            val directions = MovieFragmentDirections.actionMovieFragmentToMovieDetailsFragment(it.id!!)
            findNavController().navigate(directions)
        }
    }


    private fun observeLiveData() {
        viewModel._response.observe(viewLifecycleOwner, Observer { movies ->
            movies?.let {
                movieAdapter.movielist = it
                binding.rvMovieList.visibility = View.VISIBLE
            }
        })

        viewModel._moviesError.observe(viewLifecycleOwner, Observer { error ->
            error?.let {
                if (it) {
                    binding.tvError.visibility = View.VISIBLE
                    binding.rvMovieList.visibility = View.GONE
                } else {
                    binding.tvError.visibility = View.GONE
                    binding.rvMovieList.visibility = View.VISIBLE
                }
            }
        })

        viewModel._moviesLoading.observe(viewLifecycleOwner, Observer { loading ->
            loading?.let {
                if (it) {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.rvMovieList.visibility = View.GONE
                    binding.tvError.visibility = View.GONE
                } else {
                    binding.progressBar.visibility = View.GONE
                }
            }
        })

        viewModel.nowPlayingMovies.observe(viewLifecycleOwner) { movies ->
            adapter = ImageSliderAdapter(requireContext(), movies)
            binding.viewPager.adapter = adapter
            binding.indicator.setViewPager(binding.viewPager)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}