package com.chenyan.kotlindemo.data

data class NumberInfo(
    val number: String,
    val location: String? = null,
    val tag: String? = null,
    val source: Source = Source.LOCAL
) {
   enum class Source{
       LOCAL,CLOUD
   }
}