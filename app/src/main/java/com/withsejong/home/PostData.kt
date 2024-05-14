package com.withsejong.home

data class PostData(
//    val name:String,
//    val tag:ArrayList<String?>,
//    val image:ArrayList<String>,
//    val postTime:Int, //#분전
//    val price:Int,

    val id:Int,
    val title:String,
    val price:Int,
    val content:String,
    val studentId: String,
    val createAt:String,
    val image:ArrayList<String>,
    val tag:ArrayList<String>

)
