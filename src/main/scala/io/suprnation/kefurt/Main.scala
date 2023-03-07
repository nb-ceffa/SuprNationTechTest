package io.suprnation.kefurt

import cats.effect.{ExitCode, IO, IOApp}

object Main extends IOApp {
  override def run(args: List[String]): IO[ExitCode] = {
    IO(println("It runs!")).map(_ => ExitCode.Success)
  }
}
