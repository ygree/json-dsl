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

class PrettyJsonRenderer
(
  tab: Int = 2,
  lineCapacity: Int = 80,
  valRenderer: JsonValRenderer = StandardJsonValRenderer
  ) extends JsonRenderer 
{
  def render(json: Json) = renderImpl(json)(RenderContext())

  case class RenderContext(indentation: Int = 0) {
    def indent(symbols: Int) = RenderContext(indentation = indentation + symbols)
    def prefix = " " * indentation
    def enoughSpaceFor(line: String): Option[String] = 
      if (line.length + indentation > lineCapacity) None
      else Some(line)
  }

  import Json._

  def renderImpl(json: Json)(implicit context: RenderContext): String = json match {
    case value: Val => valRenderer.render(value)
    case Object(Nil) => "{ }"
    case Object(properties) =>
      context enoughSpaceFor 
      "{ "+renderPropertiesHorizontal(properties)+" }" getOrElse 
      "{\n"+renderPropertiesVertical(properties)+"\n"+context.prefix+"}"
    case Array(Nil) => "[ ]"
    case Array(elements) => 
      context enoughSpaceFor 
      "[ "+renderElementsHorizontal(elements)+" ]" getOrElse 
      "[\n"+renderElementsVertical(elements)+"\n"+context.prefix+"]"
  }

  def renderProperty(prop: Entry)(implicit context: RenderContext): String = {
    val (k, renderProperty) = prop
    s""""$k" : """ + renderImpl(renderProperty)
  }

  def renderPropertiesHorizontal(properties: Seq[Entry])(implicit context: RenderContext) = {
    properties map renderProperty mkString " , "
  }

  def renderPropertiesVertical(properties: Seq[Entry])(implicit context: RenderContext) = {
    val newContext = context.indent(tab)
	properties map { x => newContext.prefix + renderProperty(x)(newContext) } mkString " ,\n"
  }
  
  def renderElementsHorizontal(elements: Seq[Json])(implicit context: RenderContext) = {
    elements map renderImpl mkString " , "
  }
  
  def renderElementsVertical(elements: Seq[Json])(implicit context: RenderContext) = {
    val newContext = context.indent(tab)
	elements map { x => newContext.prefix + renderImpl(x)(newContext) } mkString " ,\n"
  }
}