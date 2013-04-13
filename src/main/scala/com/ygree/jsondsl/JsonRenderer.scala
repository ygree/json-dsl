package com.ygree.jsondsl

trait JsonRenderer {
  def render(json: Json): String
}

class CompactJsonRenderer(valRenderer: JsonValRenderer = StandardJsonValRenderer) extends JsonRenderer {

  import Json._

  def render(json: Json) = json match {
    case Array(values) => "[" + (values map render mkString ",") + "]"
    case Object(entries) => "{" + (entries map render mkString ",") + "}"
    case value: Val => valRenderer.render(value)
  }

  def render(entry: Entry): String = {
    val (k, v) = entry
    s""""$k":${render(v)}"""
  }
}

class PrettyJsonRenderer(valRenderer: JsonValRenderer = StandardJsonValRenderer) extends JsonRenderer {

  def render(json: Json) = renderImpl(json)(RenderContext())

  case class RenderContext(
    indentation: Int = 0,
    insideArray: Boolean = false,
    insideObject: Boolean = false) {
    def inObj = RenderContext(insideObject = true)
  }

  import Json._

  def renderImpl(json: Json)(implicit context: RenderContext): String = json match {
    case value: Val => valRenderer.render(value) 
    case Object(Nil) => "{ }"
    case Object(Seq((k, v))) => "{ "+renderPair(k, renderImpl(v)(context.inObj))+" }"
  }

  def renderPair(k: String, v: String): String = s""""$k" : $v"""
}