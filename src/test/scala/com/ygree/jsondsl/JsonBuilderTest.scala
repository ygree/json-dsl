package com.ygree.jsondsl

import org.specs2.mutable._
import org.junit.runner.RunWith
import org.specs2.runner.JUnitRunner
import scala.collection.immutable.ListMap

@RunWith(classOf[JUnitRunner])
class JsonBuilderTest extends Specification {

  import JsonBuilder._
  
  "JsonBuilder" should {
    "construct Json object" >> {
      obj() === Json.Object(ListMap())
      obj("a" -> 4) === Json.Object(ListMap("a" -> 4))
    }  
    "construct Json array" >> {
      array() === Json.Array(Nil)
      array("2") === Json.Array(Seq(Json.StringVal("2")))
    }
    "construct Json null" >> {
      nul() === Json.Null
    }
    "construct Json property" >> {
      prop("a" -> 5.55) === "a" -> Json.Number(5.55)
    }
    "prevent creating objects with duplicate fields" >> {
      obj("a" -> 1, "a" -> 2) must throwA[IllegalArgumentException]
    }
  }
}