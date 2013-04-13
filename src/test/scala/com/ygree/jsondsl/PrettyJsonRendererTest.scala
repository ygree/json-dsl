package com.ygree.jsondsl

import org.specs2.mutable._
import org.junit.runner.RunWith
import org.specs2.runner.JUnitRunner

@RunWith(classOf[JUnitRunner])
class PrettyJsonRendererTest extends Specification {

  import JsonBuilder._
  
  implicit val PrettyJsonRenderer = new PrettyJsonRenderer()
  
  "PrettyJsonRenderer" should {
    "render empty object in one line" >> {
      obj().render === "{ }"
    }
    "render one propery object in one line" >> {
      obj("a" -> 2).render === """{ "a" : 2 }"""
    }
    "render multiline object" >> {
      obj("a" -> 3, "b" -> 5).render === """{
        |  "a" : 3 ,
        |  "b" : 5
        |}""".stripMargin.stripLineEnd
    }
    "render empty array" >> {
      array().render === "[ ]"
    }
    "render non-empty array" >> {
      array("a", true, 2, obj()).render === """[ "a" , true , 2 , { } ]"""
    }
    
//    "render json object" >> {
//      obj(
//        "a" -> "b", 
//        "c" -> 5,
//        "o" -> obj(),
//        "e" -> array(),
//        "r" -> array("c",7,array("t"),obj("j"->obj())),
//        "n" -> nul(),
//        "b" -> false
//      ).render === """{
//                     |  "a" : "b" ,
//                     |  "c" : 5 ,
//                     |  "o" : { } ,
//                     |  "e" : [ ] ,
//                     |  "r" : [ "c", 7, [ "t" ], { "j" : { } } ] , 
//                     |  "n" : null ,
//                     |  "b" : false
//                     |}""".stripMargin
//    }
  }
}