package com.example.funflixmovieapp.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.funflixmovieapp.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}