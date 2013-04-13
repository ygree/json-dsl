package com.ygree.jsondsl

trait JsonValRenderer {
  def render(value: Json.Val): String
}

object StandardJsonValRenderer extends JsonValRenderer {
  
  import Json._
  
  def render(value: Val) = value match {
    case LongVal(value) => value.toString
    case StringVal(value) => s""""$value""""
    case Null => "null"
    case BooleanVal(value) => value.toString
  }
}