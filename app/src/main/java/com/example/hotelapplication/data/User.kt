package com.example.hotelapplication.data

data class User(
    val firstName:String,
    val lastName:String,
    val email:String,
    val phone:String,
    val imgPath:String=""
) {
    constructor():this("","","","","")
}