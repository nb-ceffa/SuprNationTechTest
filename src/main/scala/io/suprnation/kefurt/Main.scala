package io.suprnation.kefurt

import cats.effect.{ExitCode, IO, IOApp, Resource, Sync}
import cats.syntax.traverse._
import io.suprnation.kefurt.model.Triangle
import org.slf4j.LoggerFactory

import scala.io.Source

object Main extends IOApp {

  private val LOG = LoggerFactory.getLogger(Main.getClass)

  override def run(args: List[String]): IO[ExitCode] = {

    val result = for {
      file <- loadFile[IO](args)
      maybeTriangle <- Resource.eval(TriangleReader.load[IO](file))
      _ <- Resource.eval(cats.effect.std.Console[IO].println(maybeTriangle))
      _ <- maybeTriangle.traverse(triangle => Resource.eval(printMinimalPath[IO](triangle)))
    } yield ExitCode.Success

    result
      .use(IO.pure)
      .handleErrorWith { e =>
        IO.delay(LOG.warn("Main application task failed with error", e))
          .flatMap(_ => IO.raiseError(e))
      }
  }

  private def loadFile[F[_]: Sync](args: List[String]): Resource[F, Source] = {
    if (args.size < 1) {
      Resource.make(Sync[F].delay(Source.stdin))(file => Sync[F].delay(file.close()))
    } else {
      val filePath = args.head
      Resource.make(Sync[F].delay(Source.fromFile(filePath)))(file => Sync[F].delay(file.close()))
    }
  }

  private def printMinimalPath[F[_]: Sync](triangle: Triangle): F[Unit] = {
    val nodesStr = triangle.minimalPath.mkString(" + ")
    Sync[F].delay(println(s"$nodesStr = ${triangle.minimalPath.sum}"))
  }
}
