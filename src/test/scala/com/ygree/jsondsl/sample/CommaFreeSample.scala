package com.ygree.jsondsl.sample

import com.ygree.jsondsl.JsonBuilderMutable

object CommaFreeSample {
  
  import JsonBuilderMutable._
  
  def main(args: Array[String]) {

    obj {
      "a" -> 5.55
      "cde" -> true
    }
    
    //TODO 
  }
}