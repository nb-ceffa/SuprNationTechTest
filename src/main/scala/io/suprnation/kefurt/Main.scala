package io.suprnation.kefurt

import cats.effect.{ExitCode, IO, IOApp, Resource, Sync}
import org.slf4j.LoggerFactory

import scala.io.Source

object Main extends IOApp {

  private val LOG = LoggerFactory.getLogger(Main.getClass)

  override def run(args: List[String]): IO[ExitCode] = {

    val result = for {
      file <- loadFile[IO](args)
      triangle <- Resource.eval(TriangleReader.load[IO](file))
      _ <- Resource.eval(cats.effect.std.Console[IO].println(triangle))
    } yield ExitCode.Success

    result.use(IO.pure)
      .handleErrorWith { e =>
        IO.delay(LOG.warn("Main application task failed with error", e))
          .flatMap(_ => IO.raiseError(e))
      }
  }

  private def loadFile[F[_]: Sync](args: List[String]): Resource[F, Source] = {
    // TODO: use Source.stdin
    Resource.make(Sync[F].delay(Source.fromResource("data_extra_small.txt")))(file => Sync[F].delay(file.close()))
  }
}
