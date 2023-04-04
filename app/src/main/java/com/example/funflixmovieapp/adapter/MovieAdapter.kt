package com.example.funflixmovieapp.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.funflixmovieapp.databinding.ItemMovieBinding
import com.example.funflixmovieapp.model.UpcomingMovie
import com.example.funflixmovieapp.utils.Constants.POSTER_BASE_URL
import javax.inject.Inject

class MovieAdapter @Inject constructor() : RecyclerView.Adapter<MovieAdapter.ViewHolder>() {

    private lateinit var binding: ItemMovieBinding
    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        binding = ItemMovieBinding.inflate(inflater, parent, false)
        context = parent.context
        return ViewHolder()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    override fun getItemCount(): Int = differ.currentList.size

    override fun getItemViewType(position: Int): Int = position

    inner class ViewHolder : RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(item: UpcomingMovie) {
            binding.apply {
                tvMovieTitle.text = item.title
                tvMovieDate.text = item.releaseDate
                tvMovieSummary.text = item.overview
                val moviePosterUrl = POSTER_BASE_URL + item.posterPath
                Glide.with(itemView).load(moviePosterUrl).into(imgMovie)

                root.setOnClickListener {
                    onItemClickListener?.let {
                        it(item)
                    }
                }
            }
        }

    }

    private var onItemClickListener: ((UpcomingMovie) -> Unit)? = null

    fun setOnItemClickListener(listener: (UpcomingMovie) -> Unit) {
        onItemClickListener = listener
    }

    private val differCallback = object : DiffUtil.ItemCallback<UpcomingMovie>() {
        override fun areItemsTheSame(
            oldItem: UpcomingMovie,
            newItem: UpcomingMovie
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: UpcomingMovie,
            newItem: UpcomingMovie
        ): Boolean {
            return oldItem == newItem
        }
    }

    var movielist:List<UpcomingMovie>
        get() = differ.currentList
        set(value){
            differ.submitList(value)
        }

    private val differ = AsyncListDiffer(this, differCallback)

}