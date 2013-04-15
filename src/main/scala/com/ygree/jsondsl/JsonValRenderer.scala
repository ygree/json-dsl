package com.ygree.jsondsl

import scala.util.parsing.json.JSONFormat

trait JsonValRenderer {
  def render(value: Json.Val): String
}

object JsonValRendererStandard extends JsonValRenderer {
  
  import Json._
  
  def render(value: Val) = value match {
    case Number(value) => trimSuffix(value.toString, ".0")
    case StringVal(value) => '"'+JSONFormat.quoteString(value)+'"'
    case Null => "null"
    case BooleanVal(value) => value.toString
  }
  
  private def trimSuffix(s: String, suffix: String): String =
    if (s endsWith suffix) s take (s.length - suffix.length) else s
}