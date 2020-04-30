package com.example.mygram.models

import java.net.URL

data class User(
    val id:String = "",
    var username:String = "",
    var fullname: String = "",
    var bio: String = "",
    var state:String = "",
    var phone: String = "",
    var photoURL:String = "empty"
)