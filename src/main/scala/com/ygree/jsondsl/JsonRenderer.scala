package com.ygree.jsondsl

trait JsonRenderer {
  def render(json: Json): String
}

class SimpleJsonRenderer extends JsonRenderer {
  
  import Json._
  
  def render(json: Json) = json match {
    case LongVal(value) => ""+value
    case StringVal(value) => s""""$value""""
    case Array(values) => "["+(values map render mkString ",")+"]"
    case Object(entries) => "{"+(entries map render mkString ",")+"}"
  }
  
  def render(entry: Entry): String = {
    val (k, v) = entry
    s""""$k":${render(v)}"""
  } 
}

class PrettyJsonRenderer extends JsonRenderer {
  def render(json: Json) = {
    "*"
  }
}