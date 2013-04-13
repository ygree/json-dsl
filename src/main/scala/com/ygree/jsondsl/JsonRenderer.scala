package com.ygree.jsondsl

trait JsonRenderer {
  def render(json: Json): String
}

class SimpleJsonRenderer extends JsonRenderer {
  def render(json: Json) = {
    ""
  }
}

class PrettyJsonRenderer extends JsonRenderer {
  def render(json: Json) = {
    "*"
  }
}

/**
  def render(entry: Entry): String = {
    val (k, v) = entry
    s""""$k":${v.toString}"""
  } 
    override def toString = {
      val renderedFields = fields map render mkString ",\n"
      "{" + renderedFields + "}"
    }
    override def toString = s""""$value""""
    override def toString = value.toString
*/