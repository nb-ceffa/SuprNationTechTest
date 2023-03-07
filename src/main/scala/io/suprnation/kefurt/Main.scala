package io.suprnation.kefurt

import cats.data.OptionT
import cats.effect.{std, ExitCode, IO, IOApp, Resource, Sync}
import cats.implicits.toShow
import org.slf4j.LoggerFactory

object Main extends IOApp {

  private val LOG = LoggerFactory.getLogger(Main.getClass)

  override def run(args: List[String]): IO[ExitCode] = {

    load[IO](args)
      .use { triangleReader =>
        OptionT(triangleReader.getTriangle[IO])
          .subflatMap(TriangleComputer.computeMinimum)
          .map(_.show)
          .semiflatMap(std.Console[IO].println)
          .flatTapNone(std.Console[IO].println("Failed to load triangle."))
          .map(_ => ExitCode.Success)
          .getOrElse(ExitCode.Error)
      }
      .handleErrorWith { e =>
        IO.delay(LOG.warn("Main application task failed with error", e))
          .flatMap(_ => IO.raiseError(e))
      }
  }

  private def load[F[_]: Sync](args: List[String]): Resource[F, TriangleReader] = {
    if (args.size < 1) {
      TriangleReader.makeFromStdIn
    } else {
      val filePath = args.head
      TriangleReader.makeFromFile(filePath)
    }
  }
}
