package com.ygree.jsondsl

sealed trait Json

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