package com.ygree.jsondsl

trait JsonRenderer {
  def render(json: Json): String
}

class JsonRendererCompact(valRenderer: JsonValRenderer = JsonValRendererStandard) extends JsonRenderer {

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

class JsonRendererPretty
(
  tab: Int = 2,
  lineCapacity: Int = 80,
  valRenderer: JsonValRenderer = JsonValRendererStandard
  ) extends JsonRenderer 
{
  def render(json: Json) = renderImpl(json)(RenderContext())

  case class RenderContext(indentation: Int = 0, fieldNameIndentation: Int = 0) {
    def indent(symbols: Int) = copy(indentation = indentation + symbols)
    def fieldNameIndent(symbols: Int) = copy(fieldNameIndentation = symbols)
    def prefix = " " * indentation
    def leftCapacity = lineCapacity - indentation - fieldNameIndentation
    def enoughSpaceFor(line: String): Option[String] =
      if (isEnoughLeftCapacity(line)) None
      else Some(line)
    def isEnoughLeftCapacity(line: String): Boolean = line.length > leftCapacity
  }

  import Json._

  def renderImpl(json: Json)(implicit context: RenderContext): String = json match {
    case value: Val => valRenderer.render(value)
    case Object(properties) => ObjectRenderer.render(properties)
    case Array(elements) => ArrayRenderer.render(elements)
  }
  
  trait ContainerRenderer {
    type T
    
    def openBrace: String
    def closeBrace: String
    def verticalSeparator = " ,\n"
    def horizontalSeparator = " , "
    def renderedEmptyContainer = openBrace + " " + closeBrace
    
    def render(x: T)(implicit context: RenderContext): String

    def render(xs: Seq[T])(implicit context: RenderContext): String =
      if (xs.isEmpty) renderedEmptyContainer
      else context enoughSpaceFor 
      	openBrace+" "+renderHorizontal(xs)+" "+closeBrace getOrElse 
      	openBrace+"\n"+renderVertical(xs)(context.fieldNameIndent(0))+"\n"+context.prefix+closeBrace
    
    def renderHorizontal(xs: Seq[T])(implicit context: RenderContext): String = {
      xs map render mkString horizontalSeparator
    }
    
    def renderVertical(xs: Seq[T])(implicit context: RenderContext): String = {
      val newContext = context.indent(tab)
	  xs map { x => newContext.prefix + render(x)(newContext) } mkString verticalSeparator
    }
  }
  
  object ArrayRenderer extends ContainerRenderer {
    type T = Json 
    val openBrace = "["
    val closeBrace = "]"
      
    def render(x: T)(implicit context: RenderContext) = renderImpl(x)
  }

  object ObjectRenderer extends ContainerRenderer {
    type T = Entry
    val openBrace = "{"
    val closeBrace = "}"
    
    def render(x: T)(implicit context: RenderContext) = {
      val (k, renderProperty) = x
      val prefix = s""""$k" : """ 
      prefix + renderImpl(renderProperty)(context.fieldNameIndent(prefix.length))
    }
  }
}