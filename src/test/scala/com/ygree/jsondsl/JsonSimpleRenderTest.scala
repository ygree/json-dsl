package com.ygree.jsondsl

import org.specs2.mutable._
import org.junit.runner.RunWith
import org.specs2.runner.JUnitRunner

@RunWith(classOf[JUnitRunner])
class JsonSimpleRenderTest extends Specification {

  import JsonBuilder._
  implicit val JsonRenderer = new SimpleJsonRenderer
  
  "JsonSimpleRender" should {
    "render json object" >> {
      obj(
        "a" -> "b", 
        "c" -> 5,
        "o" -> obj(),
        "e" -> array(),
        "r" -> array("c",7,array("t"),obj("j"->obj())),
        "n" -> nul()
      ).render === """{"a":"b","c":5,"o":{},"e":[],"r":["c",7,["t"],{"j":{}}],"n":null"""
    }
  }
}