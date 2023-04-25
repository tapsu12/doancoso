package com.example.hotelapplication.util

import android.view.View
import androidx.fragment.app.Fragment
import com.example.hotelapplication.activity.MainActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

fun Fragment.hideBottomNavigationView(){
    val bottomNavigationView= (activity as MainActivity).findViewById<BottomNavigationView>(
        com.example.hotelapplication.R.id.bottomNavigation)
    bottomNavigationView.visibility= android.view.View.GONE
}
fun Fragment.showBottomNavigationView(){
    val bottomNavigationView= (activity as MainActivity).findViewById<BottomNavigationView>(
        com.example.hotelapplication.R.id.bottomNavigation)
    bottomNavigationView.visibility= android.view.View.VISIBLE
}


