name := "SuprNation tech test"

version := "1.0"

scalaVersion := "2.13.10"

libraryDependencies ++= Dependencies.Cats.all ++ Dependencies.Log.all
libraryDependencies += Dependencies.Test.scalaTest % Test
