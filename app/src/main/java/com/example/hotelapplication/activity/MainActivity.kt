package com.example.hotelapplication.activity


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.hotelapplication.R
import com.example.hotelapplication.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

        val binding by lazy{
            ActivityMainBinding.inflate(layoutInflater)
        }

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(binding.root)
            val navController  = findNavController(R.id.hotelHostFragment)
            binding.bottomNavigation.setupWithNavController(navController)

        }
    }
