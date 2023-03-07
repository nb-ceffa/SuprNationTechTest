package io.suprnation.kefurt

import cats.effect.{Resource, Sync}
import io.suprnation.kefurt.TriangleReader.separator

import scala.io.Source
import scala.util.Try

class TriangleReader private (input: Source) {

  def getTriangle[F[_]: Sync]: F[Option[Triangle]] = {
    Sync[F].delay {
      Try(input.getLines.map(line => line.split(separator).map(_.toInt)).toArray).toOption
    }
  }
}

object TriangleReader {

  private val separator = " "

  def makeFromFile[F[_]: Sync](filePath: String): Resource[F, TriangleReader] = {
    Resource.make(Sync[F].delay(Source.fromFile(filePath)))(file => Sync[F].delay(file.close())).map(new TriangleReader(_))
  }

  def makeFromStdIn[F[_]: Sync]: Resource[F, TriangleReader] = {
    Resource.fromAutoCloseable(Sync[F].delay(Source.stdin)).map(new TriangleReader(_))
  }
}
