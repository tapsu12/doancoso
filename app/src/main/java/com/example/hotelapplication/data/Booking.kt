package com.example.hotelapplication.data


import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.time.LocalDate
import java.util.*
@Parcelize
data class Booking(
    val idbooking:String,
    val namehotel:String="",
    val namebooking: String="",
    val cmnd:String="",
    val hotelbooking:List<TypeRoom>,
    val totalPrice:Long=0,
    val date: Date,
    val dateCheckin:Date,
    val dateCheckout:Date,
    val numberdate:Int,

): Parcelable {
    constructor():this("","","","", emptyList<TypeRoom>(),0,Date(),Date(),Date(),0)
}