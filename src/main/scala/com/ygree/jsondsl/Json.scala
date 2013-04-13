package com.ygree.jsondsl

sealed trait Json

object Json {
  type Key = String
  type Entry = (Key, Json)
  
  lazy val DefaultRenderer = new PrettyJsonRenderer
  
  implicit class Renderable(val json: Json) extends AnyVal {
    def render(implicit renderer: JsonRenderer = DefaultRenderer): String = renderer.render(json)
  }
  
  case class Object(fields: Map[Key, Json]) extends Json {
    //TODO access methods
  }
  case class Array(values: Seq[Json]) extends Json {
    //TODO access methods
  }
  
  trait Val extends Json {
    def value: Any
  }
  
  case class StringVal(value: String) extends Val
  implicit def fromString(value: String): StringVal = StringVal(value)
  
  //TODO need NumberVal with deserialization ability
  case class LongVal(value: Long) extends Val
  implicit def fromLong(value: Long): LongVal = LongVal(value)
}