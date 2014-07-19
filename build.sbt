name := "xwp-template"

organization := "com.earldouglas"

version := "0.1.0-SNAPSHOT"

scalaVersion := "2.10.4"

jetty()

libraryDependencies += "javax.servlet" % "javax.servlet-api" % "3.1.0" % "provided"

libraryDependencies ++= Seq( // test
    "org.eclipse.jetty" % "jetty-webapp" % "9.1.0.v20131115" % "test"
  , "org.eclipse.jetty" % "jetty-plus" % "9.1.0.v20131115" % "test"
  , "javax.servlet" % "javax.servlet-api" % "3.1.0" % "test"
  , "org.scalatest" %% "scalatest" % "1.9.1" % "test"
)

ScoverageSbtPlugin.instrumentSettings

CoverallsPlugin.coverallsSettings

val linkWar = taskKey[Unit]("Symlink the packaged .war file")

linkWar := {
  val (art, pkg) = packagedArtifact.in(Compile, packageWar).value
  import java.nio.file.Files
  val link = (target.value / (art.name + "." + art.extension))
  link.delete
  Files.createSymbolicLink(link.toPath, pkg.toPath)
}
