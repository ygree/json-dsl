package com.ygree.jsondsl

import scala.collection.immutable.ListMap

object JsonBuilder {
  import Json._ 
  
  def obj(properties: Property*) = {
    //TODO check unique of properties
    Object(ListMap(properties: _*))
  }
  
  def array(elements: Json*) = Array(elements.toSeq)
  
  def nul() = Null
}