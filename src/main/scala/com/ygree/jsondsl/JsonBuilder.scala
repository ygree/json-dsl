package com.ygree.jsondsl

object JsonBuilder {
  import Json._ 
  
  def obj(entries: Entry*) = Object(entries.toSeq)
  
  def array(values: Json*) = Array(values.toSeq)
  
  def nul() = Null
}