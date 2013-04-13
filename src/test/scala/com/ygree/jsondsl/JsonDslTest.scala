package com.ygree.jsondsl

import org.specs2.mutable._
import org.junit.runner.RunWith
import org.specs2.runner.JUnitRunner

@RunWith(classOf[JUnitRunner])
class JsonDslTest extends Specification {

  import JsonBuilder._
  
  "JsonBuilder" should {
    "help to construct Json objects" >> {
      obj() === Json.Object(Nil)
      obj("a" -> 4) === Json.Object(Seq("a" -> 4))
      
      array() === Json.Array(Nil)
      array("2") === Json.Array(Seq(Json.StringVal("2")))
      
      nul() === Json.Null
    }
  }

  "Json.Object" should {
    "let access members" >> {
      todo
    }
    "compare json objects" >> {
      todo
    }
  }

  "Json.render" should {
    "delegate rendering to implicit JsonRenderer in scope" >> {
      implicit val renderer = new JsonRenderer {
        def render(json: Json): String = "exp324"
      }
      Json.Object(Nil).render === "exp324" //TODO use ScalaMock
    }
  }
}