package com.example.hotelapplication.data


import java.time.LocalDate
import java.util.*

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
    val numberdate:Int
) {
}