package com.ygree.jsondsl

import org.specs2.mutable._
import org.junit.runner.RunWith
import org.specs2.runner.JUnitRunner
import scala.collection.immutable.ListMap

@RunWith(classOf[JUnitRunner])
class JsonTest extends Specification {
  
  import JsonConciseBuilder._
    
  val fifteen = 15L
  
  val json = jo(
    "amount" -> 44.55 ,
	"ids" -> ja(1,2,fifteen,99,36,true,"55"),
	"books" -> ja(
	  jo(
	    "title" -> "Book Title #1",
	    "author" -> jo(
	      "name" -> "Author #1",
	      "birthday" -> "1953-05-23"
	    )
	  )
	)
  )
  
  "Json.Object" should {
    "let access members" >> {
      json.$("ids", "2") === jv(fifteen) //TODO 
    }
    "compare json objects" >> {
      todo
    }
  }
}