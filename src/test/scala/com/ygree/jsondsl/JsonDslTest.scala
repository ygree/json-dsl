package com.ygree.jsondsl

import org.specs2.mutable._
import org.junit.runner.RunWith
import org.specs2.runner.JUnitRunner

@RunWith(classOf[JUnitRunner])
class JsonDslTest extends Specification {

  import JsonBuilder._
//  implicit val JsonRenderer = new SimpleJsonRenderer
  
  "Json.Object" should {
    "let access members" >> {
      todo
    }
    "compare json objects" >> {
      todo
    }
  }
  
  def test {
    val json = obj(
      "a" -> "b",
      "c" -> "d",
      "o" -> obj(
        "1" -> "a"
      ),
      "arr" -> array(1, 2, "j", obj())
    )
  }
}