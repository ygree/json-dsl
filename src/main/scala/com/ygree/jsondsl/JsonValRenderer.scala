package com.ygree.jsondsl

trait JsonValRenderer {
  def render(value: Json.Val): String
}

object JsonValRendererStandard extends JsonValRenderer {
  
  import Json._
  
  def render(value: Val) = value match {
    case LongVal(value) => value.toString
    case DoubleVal(value) => value.toString
    case StringVal(value) => s""""$value"""" //TODO escape
    case Null => "null"
    case BooleanVal(value) => value.toString
  }
}