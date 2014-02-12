name := "xwp-template"

organization := "com.earldouglas"

version := "0.1.0-SNAPSHOT"

scalaVersion := "2.10.3"

seq(webSettings :_*)

libraryDependencies += "org.eclipse.jetty" % "jetty-webapp" % "9.1.0.v20131115" % "container"

libraryDependencies += "org.eclipse.jetty" % "jetty-plus" % "9.1.0.v20131115" % "container"

libraryDependencies += "javax.servlet" % "servlet-api" % "2.5" % "provided"

libraryDependencies += "com.google.guava" % "guava" % "15.0"

warPostProcess in Compile <<= fullClasspath in Compile map { fc =>
  { x =>
    // Find the library dependencies
    val libs: Seq[java.io.File] =
      fc.map(_.data) filter { x => x.exists && !x.isDirectory }
    // Create a regular expression to match a desired library
    val regex = """guava.*jar""".r
    // Find the desired library dependency
    val toMove: Option[java.io.File] =
      libs find { f => regex.pattern.matcher(f.getName).matches }
    // Move the desired library dependency under WEB-INF/plugins
    toMove foreach { lib =>
      IO.move(x / "WEB-INF" / "lib"     / lib.getName,
              x / "WEB-INF" / "plugins" / lib.getName)
    }
  }
}
