package com.example.hotelapplication.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Hotel(
    val id: String,
    val name: String,
    val idAdmin: String,
    val price: Int,
    val address:String,
    val addressdetail: String?=null,
    val offerPercentage: Float? = 0f,
    val description: String? = null,
    val typeRoom: List<TypeRoom>,
    val images: List<String>,

    ): Parcelable {
    constructor() : this( "0","" ,"",0,"","",0f,"", typeRoom = emptyList(),images = emptyList())
}