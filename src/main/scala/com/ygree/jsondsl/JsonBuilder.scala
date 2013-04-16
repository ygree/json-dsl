package com.ygree.jsondsl

import scala.collection.immutable.ListMap

object JsonBuilder {
  import Json._ 
  
  def obj(properties: Property*) = {
    val dupKeys = properties.groupBy(_._1) collect {
      case (k, ps) if ps.size > 1 => k
    }
    require(
      dupKeys.isEmpty, 
      "Json.Object creating impossible, properties can't contain duplicate keys: "+dupKeys.mkString(", ")
    )
    Object(ListMap(properties: _*))
  }
  
  def array(elements: Json*) = Array(elements.toSeq)
  
  def nul() = Null
  
  def prop(p: Property): Property = p

  def value[T <% Json.Val](v: T): Json.Val = v
}

object JsonConciseBuilder {
  import Json._ 
  import JsonBuilder._
  
  def jo(ps: Property*) = obj(ps: _*)
  
  def ja(es: Json*) = array(es: _*)
  
  def jn = nul()
  
  def jp(p: Property) = prop(p)
  
  def jv[T <% Json.Val](v: T): Json.Val = v
}