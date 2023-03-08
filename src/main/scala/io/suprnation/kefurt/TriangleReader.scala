package io.suprnation.kefurt

import cats.effect.{Resource, Sync}
import cats.syntax.functor._
import io.suprnation.kefurt.TriangleReader.separator

import scala.annotation.tailrec
import scala.io.{Source, StdIn}
import scala.util.Try

class TriangleReader private (input: Source) {

  def getTriangle[F[_]: Sync]: F[Option[Triangle]] = {
    Sync[F].delay(input.getLines()).map { lines =>
      Try(lines.toList.map(line => line.split(separator).map(_.toInt)).toArray).toOption
    }
  }
}

object TriangleReader {

  private val separator = " "

  def makeFromFile[F[_]: Sync](filePath: String): Resource[F, TriangleReader] = {
    Resource.make(Sync[F].delay(Source.fromFile(filePath)))(file => Sync[F].delay(file.close())).map(new TriangleReader(_))
  }

  def makeFromStdIn[F[_]: Sync]: Resource[F, TriangleReader] = {
    Resource
      .eval(Sync[F].delay(readLinesFromStdIn))
      .map(Source.fromString)
      .map(new TriangleReader(_))
  }

  private def readLinesFromStdIn: String = {
    readLinesFromStdIn(List.empty).reverse.mkString("\n")
  }
  @tailrec
  private def readLinesFromStdIn(acc: List[String]): List[String] = {
    Option(StdIn.readLine()) match {
      case Some(value) if value.nonEmpty => readLinesFromStdIn(acc.prepended(value))
      case _ => acc
    }
  }

  def makeFromString[F[_]: Sync](str: String): Resource[F, TriangleReader] = {
    Resource.pure(Source.fromString(str)).map(new TriangleReader(_))
  }
}
