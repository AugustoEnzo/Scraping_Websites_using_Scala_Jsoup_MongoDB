ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.0"

lazy val root = (project in file("."))
  .settings(
    name := "Scraping Websites using Scala and Jsoup"
  )

libraryDependencies += "org.jsoup" % "jsoup" % "1.15.3"
libraryDependencies += "org.mongodb.scala" %% "mongo-scala-driver" % "4.7.2"
