package io.suprnation.kefurt

import cats.effect.Sync
import cats.data.OptionT
import io.suprnation.kefurt.model.{End, Node, Triangle}

import scala.io.Source
import scala.util.Try

object TriangleReader {

  type SerializedTriangle = Array[Array[Int]]

  private val separator = " "

  def load[F[_]: Sync](input: Source): F[Option[Triangle]] = {
    val result = for {
      serializedTriangle <- OptionT(parse(input))
    } yield createTriangle(serializedTriangle)

    result.value
  }

  private def parse[F[_]: Sync](input: Source): F[Option[SerializedTriangle]] = {
    Sync[F].delay{
      Try(input.getLines.map(line => line.split(separator).map(_.toInt)).toArray).toOption
    }
  }

  private def createTriangle(input: SerializedTriangle): Triangle = {
    createTriangle(input, 0, 0)
  }
  private def createTriangle(input: SerializedTriangle, lineIndex: Int, nodeIndex: Int): Triangle = {
    if (lineIndex == input.length - 1) {
      // last line
      End(input(lineIndex)(nodeIndex))
    } else {
      val leftTriangle = createTriangle(input, lineIndex + 1, nodeIndex)
      val rightTriangle = createTriangle(input, lineIndex + 1, nodeIndex + 1)
      val nodeValue = input(lineIndex)(nodeIndex)
      Node(leftTriangle, rightTriangle, nodeValue)
    }
  }
}
