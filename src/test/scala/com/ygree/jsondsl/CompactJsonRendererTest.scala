package com.ygree.jsondsl

import org.specs2.mutable._
import org.junit.runner.RunWith
import org.specs2.runner.JUnitRunner

@RunWith(classOf[JUnitRunner])
class CompactJsonRendererTest extends Specification {

  import JsonBuilder._
  
  implicit val CompactJsonRenderer = new CompactJsonRenderer()
  
  "CompactJsonRenderer" should {
    "render json object" >> {
      obj(
        "a" -> "b", 
        "c" -> 5,
        "o" -> obj(),
        "e" -> array(),
        "r" -> array("c",7,array("t"),obj("j"->obj())),
        "n" -> nul(),
        "b" -> false
      ).render === """{"a":"b","c":5,"o":{},"e":[],"r":["c",7,["t"],{"j":{}}],"n":null,"b":false}"""
    }
  }
}