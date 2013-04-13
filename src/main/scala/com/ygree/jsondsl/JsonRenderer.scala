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

  case class RenderContext(indentation: Int = 0, fieldNameIndentation: Int = 0) {
    def indent(symbols: Int) = copy(indentation = indentation + symbols)
    def fieldNameIndent(symbols: Int) = copy(fieldNameIndentation = symbols)
    def prefix = " " * indentation
    def leftCapacity = lineCapacity - indentation - fieldNameIndentation
    def enoughSpaceFor(line: String): Option[String] = {
      println(prefix+line)
      println("^" * leftCapacity)
      if (isEnoughLeftCapacity(line)) None
      else Some(line)
    } 
    def isEnoughLeftCapacity(line: String): Boolean = line.length > leftCapacity
  }

  import Json._

  def renderImpl(json: Json)(implicit context: RenderContext): String = json match {
    case value: Val => valRenderer.render(value)
    case Object(Nil) => "{ }"
    case Object(properties) =>
      context enoughSpaceFor 
      "{ "+renderPropertiesHorizontal(properties)+" }" getOrElse 
      "{\n"+renderPropertiesVertical(properties)(context.fieldNameIndent(0))+"\n"+context.prefix+"}"
    case Array(Nil) => "[ ]"
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

  def renderProperty(prop: Entry)(implicit context: RenderContext): String = {
    val (k, renderProperty) = prop
    val prefix = s""""$k" : """ 
    prefix + renderImpl(renderProperty)(context.fieldNameIndent(prefix.length))
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

/*
java.lang.Exception: '{
  "abc" : [
  { } ,
  false ,
  "here we go" ,
  43
] ,
  "def" : null ,
  "ghj" : [
  [ [ { "a" : true } , 1 ] ]
]
}'
 is not equal to 
'{
  "abc" : [
    { } ,
    false ,
    "here we go" ,
    43
  ] ,
  "def" : null ,
  "ghj" : [ [ [ { "a" : true } , 1 ] ] ]
}'
	at com.ygree.jsondsl.PrettyJsonRendererTest$$anonfun$1$$anonfun$apply$16.apply(PrettyJsonRendererTest.scala:50)
	at com.ygree.jsondsl.PrettyJsonRendererTest$$anonfun$1$$anonfun$apply$16.apply(PrettyJsonRendererTest.scala:38)


*/