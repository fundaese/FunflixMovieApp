package com.example.funflixmovieapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.example.funflixmovieapp.R
import com.example.funflixmovieapp.databinding.FragmentMovieBinding
import com.example.funflixmovieapp.model.NowPlaying
import com.example.funflixmovieapp.model.UpcomingMovie
import com.example.funflixmovieapp.utils.Constants

class ImageSliderAdapter(
    private val context: Context,
    private val movies: List<NowPlaying.Result>
) :
    PagerAdapter() {

    private var onItemClickListener: ((NowPlaying.Result) -> Unit)? = null

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view: View =
            (context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater).inflate(
                R.layout.image_slider_item,
                null
            )
        val ivImages = view.findViewById<ImageView>(R.id.iv_images)

        val movie = movies[position]

        movie.let {
            val moviePosterUrl = Constants.POSTER_BASE_URL + movie.poster_path
            Glide.with(context).load(moviePosterUrl).into(ivImages)
        }

        val vp = container as ViewPager
        vp.addView(view, 0)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        val vp = container as ViewPager
        val view = `object` as View
        vp.removeView(view)
    }

    override fun getCount(): Int {
        return movies.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    fun setOnItemClickListener(listener: (NowPlaying.Result) -> Unit) {
        onItemClickListener = listener
    }

}

