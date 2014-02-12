import sbt._
import Keys._

object HelloBuild extends Build {

    lazy val root = Project(
        id = "root",
        base = file("."),
        settings =
          Project.defaultSettings).dependsOn(common)

    lazy val web =
      Project(
        id = "web",
        base = file("web"),
        settings =
          Project.defaultSettings).dependsOn(common)

    lazy val common =
      Project(
        id = "common",
        base = file("common"),
        settings = Project.defaultSettings)

}
