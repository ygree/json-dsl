package com.ygree.jsondsl

import org.specs2.mutable._
import org.junit.runner.RunWith
import org.specs2.runner.JUnitRunner
import scala.collection.immutable.ListMap

@RunWith(classOf[JUnitRunner])
class JsonValRendererStandardTest extends Specification {

  "JsonValRendererStandard" should {
    import JsonValRendererStandard._
    import Json._
    "escape special characters" >> {
      render(StringVal("\"")) === """"\"""""
    }
    "render real numbers" >> {
      render(Number(1.23)) === "1.23"
    }
    "render integer numbers" >> {
      render(Number(134)) === "134"
    }
  }
}