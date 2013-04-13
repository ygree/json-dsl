package com.ygree.jsondsl

import org.specs2.mutable._
import org.junit.runner.RunWith
import org.specs2.runner.JUnitRunner

//import scala.language.dynamics
@RunWith(classOf[JUnitRunner])
class JsonDslTest extends Specification {

  "Json.Object" should {
    "be rendered as empty" >> {
      J.obj().toString === "{}"
    }
    "be rendered as full" >> {
      J.obj("1" -> 2).toString === """{"1":2}""" 
    }
  }
  
  def test {
    val json = J.obj(
      "a" -> "b",
      "c" -> "d",
      "o" -> J.obj(
        "1" -> "a"
      ),
      "arr" -> J.array(1, 2, "j", J.obj())
    )
  }
}

sealed trait Json

object J {
  import Json._ 
  
  def obj(entries: Entry*): Object = {
    Object(entries.toMap)
  }
  
  def array(values: Json*): Array = {
    Array(values.toSeq)
  }
}

object Json {
  type Key = String
  type Entry = (Key, Json)
  def render(entry: Entry): String = {
    val (k, v) = entry
    s""""$k":${v.toString}"""
  } 
  
  case class Object(fields: Map[Key, Json]) extends Json {
    //TODO access methods
    override def toString = {
      val renderedFields = fields map render mkString ",\n"
      "{" + renderedFields + "}"
    }
  }
  case class Array(values: Seq[Json]) extends Json {
    //TODO access methods
  }
  
  trait Val extends Json {
    def value: Any
  }
  
  case class StringVal(value: String) extends Val {
    override def toString = s""""$value""""
  }
  implicit def fromString(value: String): StringVal = StringVal(value)
  
  //TODO need NumberVal with deserialization ability
  case class LongVal(value: Long) extends Val {
    override def toString = value.toString
  }
  implicit def fromLong(value: Long): LongVal = LongVal(value)
}