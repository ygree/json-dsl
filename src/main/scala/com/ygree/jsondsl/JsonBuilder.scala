package com.ygree.jsondsl

object JsonBuilder {
  import Json._ 
  
  def obj(entries: Entry*): Object = {
    Object(entries.toSeq)
  }
  
  def array(values: Json*): Array = {
    Array(values.toSeq)
  }
}