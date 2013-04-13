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
    isElement: Boolean = false,
    isProperty: Boolean = false) {
    def itsProperty = RenderContext(isProperty = true)
    def indent(symbols: Int) = RenderContext(indentation = indentation + symbols)
    def prefix = " " * indentation
  }

  import Json._

  def renderImpl(json: Json)(implicit context: RenderContext): String = json match {
    case value: Val => valRenderer.render(value)
    case Object(Nil) => "{ }"
    case Object(Seq(property)) => "{ " + renderProperty(property) + " }"
    case Object(properties) =>
      context.prefix + "{\n" +
        renderPropertiesVertical(properties) +
        context.prefix + "\n}"
    case Array(Nil) => "[ ]"
    case Array(elements) => "[ "+renderElementsHorizontal(elements)+" ]"
  }

  def renderProperty(prop: Entry)(implicit context: RenderContext): String = {
    val (k, renderProperty) = prop
    val v = renderImpl(renderProperty)(context.itsProperty)
    val prefix = s""""$k" : """
    context.prefix + prefix + v
  }

  def renderElement(element: Json)(implicit context: RenderContext): String = {
    renderImpl(element) //(context.itsElement)
  }

  def renderPropertiesVertical(properties: Seq[Entry])(implicit context: RenderContext) = {
    properties map { x => renderProperty(x)(context.indent(2)) } mkString " ,\n"
  }

  def renderElementsHorizontal(elements: Seq[Json])(implicit context: RenderContext) = {
    elements map { x => renderElement(x) } mkString " , "
  }
}
