package com.ygree.jsondsl

trait JsonRenderer {
  def render(json: Json): String
}

class CompactJsonRenderer(valRenderer: JsonValRenderer = StandardJsonValRenderer) extends JsonRenderer {
  
  import Json._
  
  def render(json: Json) = json match {
    case Array(values) => "["+(values map render mkString ",")+"]"
    case Object(entries) => "{"+(entries map render mkString ",")+"}"
    case value: Val => valRenderer.render(value)
  }
  
  def render(entry: Entry): String = {
    val (k, v) = entry
    s""""$k":${render(v)}"""
  } 
}

class PrettyJsonRenderer extends JsonRenderer {
  def render(json: Json) = {
    ???
  }
}