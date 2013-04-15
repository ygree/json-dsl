package com.ygree.jsondsl

import scala.util.parsing.json.JSONFormat

trait JsonValRenderer {
  def render(value: Json.Val): String
}

object JsonValRendererStandard extends JsonValRenderer {
  
  import Json._
  
  def render(value: Val) = value match {
    case Number(value) => 
      val r = value.toString
      if (r.endsWith(".0")) r.take(r.length - 2) else r
    case StringVal(value) => '"'+JSONFormat.quoteString(value)+'"'
    case Null => "null"
    case BooleanVal(value) => value.toString
  }
}