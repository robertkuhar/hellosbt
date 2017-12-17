import Dependencies._

lazy val root = (project in file("."))
  .settings(
    name := "HelloSBT",
    organization := "com.example",
    scalaVersion := "2.12.3",
    version := "0.1.0-SNAPSHOT",
    hello := {
      println("Hello!")
      "OK"
    },
    libraryDependencies += scalaTest % Test
  )

lazy val hello = taskKey[String]("An example task")
