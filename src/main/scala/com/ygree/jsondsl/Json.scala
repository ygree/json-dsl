package com.ygree.jsondsl

sealed trait Json

object Json {
  type Key = String
  type Entry = (Key, Json)
  
  lazy val DefaultRenderer = new PrettyJsonRenderer
  
  implicit class Renderable(val json: Json) extends AnyVal {
    def render(implicit renderer: JsonRenderer = DefaultRenderer): String = renderer.render(json)
  }
  
  case class Object(fields: Seq[Entry]) extends Json
  case class Array(values: Seq[Json]) extends Json
  
  sealed trait Val extends Json
  
  case class StringVal(value: String) extends Val
  implicit def fromString(value: String): StringVal = StringVal(value)
  
  sealed trait Number extends Val
  case class LongVal(value: Long) extends Number
  implicit def fromLong(value: Long): LongVal = LongVal(value)
  
  object Null extends Val
  //TODO Double
  //TODO Boolean
}