package com.sample.hackitdemo.network.models


data class Image(
    val id:String,
    val description:String,
    val urls:Urls,
    val links: Links,
    var isLiked:Boolean = false
)

data class Urls(
    val raw:String,
    val full:String,
    val regular:String,
    val thumb:String,
    val small:String,
    val smalls3:String
)

data class Links(
    val download:String,
    val downloadLocation:String
)
