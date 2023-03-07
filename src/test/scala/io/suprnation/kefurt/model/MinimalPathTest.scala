package io.suprnation.kefurt.model

import cats.implicits.toShow
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should

class MinimalPathTest extends AnyFlatSpec with should.Matchers {

  behavior of "MinimalPath"

  it should "serialize result in correct format" in {
    MinimalPath(18, List(7, 6, 3, 2)).show shouldBe "7 + 6 + 3 + 2 = 18"
  }
}
