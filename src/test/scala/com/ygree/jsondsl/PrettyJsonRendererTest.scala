package com.ygree.jsondsl

import org.specs2.mutable._
import org.junit.runner.RunWith
import org.specs2.runner.JUnitRunner

@RunWith(classOf[JUnitRunner])
class PrettyJsonRendererTest extends Specification {

  import JsonBuilder._
  
  implicit val PrettyJsonRenderer = new PrettyJsonRenderer(lineCapacity = 11)
  
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
        |}""".stripMargin
    }
    "render empty array" >> {
      array().render === "[ ]"
    }
    "render non-empty array" >> {
      array("a", true, 2, obj()).render === """[
    	|  "a" ,
        |  true ,
        |  2 ,
        |  { }
        |]""".stripMargin
    }
    "render complex object" >> {
      val r = new PrettyJsonRenderer(lineCapacity = 40)
      obj(
        "abc" -> array(
          obj(
          ),
          false,
          "here we go",
          43
        ),
        "def" -> nul(),
        "ghj" -> array(array(array(obj("a"->true), 1)))
      ).render(r) === """{
        |  "abc" : [
        |    { } ,
        |    false ,
        |    "here we go" ,
        |    43
        |  ] ,
        |  "def" : null ,
        |  "ghj" : [ [ [ { "a" : true } , 1 ] ] ]
        |}""".stripMargin
    }
  }
}