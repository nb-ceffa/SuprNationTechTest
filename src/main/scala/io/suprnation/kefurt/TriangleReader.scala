package io.suprnation.kefurt

import cats.effect.Sync
import cats.data.OptionT

import scala.io.Source
import scala.util.Try

object TriangleReader {

  type SerializedTriangle = Array[Array[Int]]

  private val separator = " "

  def load[F[_]: Sync](input: Source): F[Option[Node]] = {
    val result = for {
      serializedTriangle <- OptionT(parse(input))
      result <- OptionT.fromOption(createTriangle(serializedTriangle))
    } yield result

    result.value
  }

  private def parse[F[_]: Sync](input: Source): F[Option[SerializedTriangle]] = {
    Sync[F].delay {
      Try(input.getLines.map(line => line.split(separator).map(_.toInt)).toArray).toOption
    }
  }

  case class Node(sumValue: Int, path: List[Int])

  private def createTriangle(input: SerializedTriangle): Option[Node] = {
    val bottom = input(input.length - 1).map(value => Node(value, List(value))) // last row (input.length - 1 == last index)
    val upperRows = input.take(input.length - 1) // input.length - 1 == ignore last

    upperRows
      .foldRight(bottom)((currentLine, previousLine) => {
        val selectedValues = computeMinOfTwoNeighboringValues(previousLine)
        selectedValues.zip(currentLine).map { case (minValue, currentValue) =>
          Node(minValue.sumValue + currentValue, minValue.path.prepended(currentValue))
        }
      })
      .headOption
  }

  private def computeMinOfTwoNeighboringValues(previousLine: Array[Node]): Array[Node] = {
    /*
    val line = [1, 2, 3, 4]
    line.zip(line.tail) === [(1,2),(2,3),(3,4)]
     */
    previousLine.zip(previousLine.tail).map(pair => if (pair._1.sumValue < pair._2.sumValue) pair._1 else pair._2)
  }
}
