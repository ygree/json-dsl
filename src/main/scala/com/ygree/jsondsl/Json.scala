package com.ygree.jsondsl

import scala.collection.immutable.ListMap

sealed trait Json

object Json {
  type Key = String
  type Property = (Key, Json)
  
  case class Object(properties: ListMap[Key, Json]) extends Json
  case class Array(elements: Seq[Json]) extends Json
  
  sealed trait Val extends Json
  
  case class StringVal(value: String) extends Val
  implicit def fromString(value: String): StringVal = StringVal(value)
  
  sealed trait Number extends Val
  
  case class LongVal(value: Long) extends Number
  implicit def fromLong(value: Long): LongVal = LongVal(value)
  
  case class DoubleVal(value: Double) extends Number
  implicit def fromDouble(value: Double): DoubleVal = DoubleVal(value)
  
  object Null extends Val
  
  case class BooleanVal(value: Boolean) extends Number
  implicit def fromBoolean(value: Boolean): BooleanVal = BooleanVal(value)

  object Renderers {
    lazy val Pretty = new JsonRendererPretty
    lazy val Compact = new JsonRendererCompact
    lazy val Default = Pretty
  }
  
  implicit class Renderable(val json: Json) extends AnyVal {
    import Renderers._
    def render(implicit renderer: JsonRenderer = Default): String = renderer.render(json)
    def pretty = render(Pretty)
    def compact = render(Compact)
  }
  
  implicit class Selectable(val json: Json) extends AnyVal {
    def $(path: String*): Json =
      if (path.isEmpty) json
      else json match {
        case Object(ps) => ps(path.head).$(path.tail: _*)
        case Array(es) => es(path.head.toInt).$(path.tail: _*)
      }
  }
}