import sbt._

object Dependencies {
  object Cats {
    val effect = "org.typelevel" %% "cats-effect" % "3.4.8"
    val core = "org.typelevel" %% "cats-core" % "2.9.0"

    val all = Seq(effect, core)
  }

  object Log {
    private val log4catsVersion = "2.5.0"
    val core = "org.typelevel" %% "log4cats-core" % log4catsVersion
    val slf4j = "org.typelevel" %% "log4cats-slf4j" % log4catsVersion

    val all = Seq(core, slf4j)
  }

  object Test {
    val scalaTest = "org.scalatest" %% "scalatest" % "3.2.15"
  }
}
