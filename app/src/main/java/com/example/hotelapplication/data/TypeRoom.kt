package com.example.hotelapplication.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TypeRoom(
    val name:String="",
    val price:Long=0,
    val description:String="",
    val quantity:Int=0
):Parcelable
