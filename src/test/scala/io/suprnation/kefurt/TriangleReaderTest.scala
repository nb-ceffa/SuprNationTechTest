package io.suprnation.kefurt

import cats.effect.IO
import cats.effect.unsafe.IORuntime
import org.scalatest
import org.scalatest.flatspec.AsyncFlatSpec
import org.scalatest.matchers.should

import scala.concurrent.ExecutionContext

class TriangleReaderTest extends AsyncFlatSpec with should.Matchers {

  behavior of "TriangleReader"

  implicit lazy val ioRuntime: IORuntime = {
    val context = ExecutionContext.global
    IORuntime(
      compute = context,
      blocking = context,
      scheduler = IORuntime.global.scheduler,
      shutdown = () => (),
      config = IORuntime.global.config
    )
  }

  it should "correctly parse a triangle" in {
    val exampleTriangle = """7
                            |6 3
                            |3 8 5
                            |11 2 10 9
                            |""".stripMargin

    val expectedArray = Array(
      Array(7),
      Array(6, 3),
      Array(3, 8, 5),
      Array(11, 2, 10, 9)
    )

    test(exampleTriangle) { maybeTriangle =>
      maybeTriangle.isDefined shouldBe true
      maybeTriangle.get shouldEqual expectedArray
    }
  }

  it should "ignore malformed triangle" in {
    val malformedTriangle =
      """7
        |6 3
        |3 X 5
        |11 2 10 9
        |""".stripMargin

    test(malformedTriangle) { maybeTriangle =>
      maybeTriangle.isDefined shouldBe false
    }
  }

  private def test(input: String)(tests: Option[Triangle] => scalatest.Assertion) = {
    TriangleReader
      .makeFromString[IO](input)
      .use(reader => reader.getTriangle[IO].map(tests))
      .unsafeToFuture()(ioRuntime)
  }
}
