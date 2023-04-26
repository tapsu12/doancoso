package com.example.hotelapplication.data

data class User(
    val firstName:String,
    val lastName:String,
    val email:String,
    val phone:String,
    val role:String="user",
    val imgPath:String=""
) {
    constructor():this("","","","","user","")
}