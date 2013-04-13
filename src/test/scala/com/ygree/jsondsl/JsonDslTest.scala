package com.ygree.jsondsl

import org.specs2.mutable._
import org.junit.runner.RunWith
import org.specs2.runner.JUnitRunner

//import scala.language.dynamics
@RunWith(classOf[JUnitRunner])
class JsonDslTest extends Specification {

  import JsonBuilder._
  
  "Json.Object" should {
    "be rendered as empty" >> {
      obj().toString === "{}"
    }
    "be rendered as full" >> {
      obj("1" -> 2).toString === """{"1":2}""" 
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

