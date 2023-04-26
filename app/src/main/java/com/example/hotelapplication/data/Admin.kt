package com.example.hotelapplication.data

data class Admin(
    val firstName:String,
    val lastName:String,
    val email:String,
    val phone:String,
    val role:String="admin",
    val imgPath:String="",
    val hotelList: List<String>
) {
    constructor():this("","","","","admin","", emptyList())
}