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
  
  case class Number(value: Double) extends Val
  implicit def fromLong(value: Long): Number = Number(value.toDouble)
  implicit def fromDouble(value: Double): Number = Number(value)
  
  object Null extends Val
  
  case class BooleanVal(value: Boolean) extends Val
  implicit def fromBoolean(value: Boolean): BooleanVal = BooleanVal(value)

  object Renderers {
    lazy val Pretty = new JsonRendererPretty
    lazy val Compact = new JsonRendererCompact
    lazy val Default = Pretty
  }
  
  implicit def toRenderable(json: Json) = new Renderable(json)
  class Renderable(json: Json) {
    import Renderers._
    def render(implicit renderer: JsonRenderer = Default): String = renderer.render(json)
    def pretty = render(Pretty)
    def compact = render(Compact)
  }
  
  implicit def toSelectable(json: Json) = new Selectable(json) 
  class Selectable(json: Json) {
    def $(path: String*): Json =
      if (path.isEmpty) json
      else json match {
        case Object(ps) => ps(path.head).$(path.tail: _*)
        case Array(es) => es(path.head.toInt).$(path.tail: _*)
      }
  }
}