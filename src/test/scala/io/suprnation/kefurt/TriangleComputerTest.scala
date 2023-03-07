package io.suprnation.kefurt

import io.suprnation.kefurt.model.MinimalPath
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should

import scala.util.Random

class TriangleComputerTest extends AnyFlatSpec with should.Matchers {

  behavior of "TriangleComputer"

  it should "return None if triangle is empty" in {
    TriangleComputer.computeMinimum(Array.empty) shouldBe None
  }

  it should "return result for one line triangle" in {
    TriangleComputer.computeMinimum(Array(Array(5))) shouldBe Some(MinimalPath(5, List(5)))
  }

  it should "return correct minimal path" in {
    val exampleTriangle = Array(
      Array(7),
      Array(6, 3),
      Array(3, 8, 5),
      Array(11, 2, 10, 9)
    )

    TriangleComputer.computeMinimum(exampleTriangle) shouldBe Some(MinimalPath(18, List(7, 6, 3, 2)))
  }

  it should "return minimal path for large triangle" in {
    val largeTriangle = (for {
      lines <- 1 to 8000
    } yield Array.fill(lines)(Random.nextInt)).toArray

    TriangleComputer.computeMinimum(largeTriangle).isDefined shouldBe true
  }
}
