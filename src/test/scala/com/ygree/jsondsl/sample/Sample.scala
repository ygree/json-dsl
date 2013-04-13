package com.ygree.jsondsl.sample

import com.ygree.jsondsl.JsonBuilder

object Sample {

  import JsonBuilder._
  
  def main(args: Array[String]) {

    val title2 = prop("title" -> "Book Title #2")
    val birthday2 = prop("birthday" -> "1923-04-23")
    
    val book2 = obj(
      title2,
      "author" -> obj(
        "name" -> "Author #5",
        birthday2
      )
    )  
    
    val json = obj(
      "amount" -> 44.55 ,
      "ids" -> array(1,2,15,99,36,true,"55"),
      "books" -> array(
        obj(
          "title" -> "Book Title #1",
          "author" -> obj(
            "name" -> "Author #1",
            "birthday" -> "1953-05-23"
          )
        ),
        book2
      )
    )
    
    println(json.pretty)
    println(json.compact)
  }
}