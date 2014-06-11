package com.earldouglas.xwptemplate

import org.scalatest._

import org.eclipse.jetty.server.Server
import org.eclipse.jetty.server.ServerConnector
import org.eclipse.jetty.webapp.WebAppContext

class TestServer(war: String, port: Int, context: String) {

  val server = new Server()

  val conn = new ServerConnector(server)
  conn.setHost("localhost")
  conn.setPort(port)
  server.addConnector(conn)
 
  val webapp = new WebAppContext()
  webapp.setContextPath(context)
  webapp.setWar(war)
  server.setHandler(webapp)

  server.start()

  def stop() {
    server.stop()
  }

}

class ServletTests extends FunSuite with BeforeAndAfterAll {

  val testServer = new TestServer("src/main/webapp", 8080, "/")

  override def afterAll() {
    testServer.stop()
  }

  def get(url: String): String = {

    import java.io._
    import java.net._
    import scala.io.Source.fromInputStream

    val conn = (new URL(url)).openConnection.asInstanceOf[HttpURLConnection]
    conn.setRequestMethod("GET")
    fromInputStream(conn.getInputStream).getLines().mkString("\n")
  }

  test("GET /") {
    val res = get("http://localhost:8080/")
    assert(res === """<html><body><h1>Hello, world!</h1></body></html>""")
  }

}
