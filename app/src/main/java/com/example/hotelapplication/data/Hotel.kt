package com.example.hotelapplication.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Hotel(
    val id: String,
    val name: String,
    val category: String,
    val price: Float,
    val address:String,
    val offerPercentage: Float? = 0f,
    val description: String? = null,
    val typeRoom: List<TypeRoom>,
    val images: List<String>,

): Parcelable {
    constructor() : this( "0","" ,"",0f,"", typeRoom = emptyList(),images = emptyList())
}