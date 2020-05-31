package com.example.mygram.models


data class CommonModel(
    val id:String = "",
    var username:String = "",
    var fullname: String = "",
    var bio: String = "",
    var state:String = "",
    var phone: String = "",
    var photoURL:String = "empty",

    var text: String = "",
    var type: String = "",
    var from: String = "",
    var timestamp: Any = 0
)