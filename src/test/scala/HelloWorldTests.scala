package com.earldouglas.xwptemplate

import java.io._
import java.net._

import org.scalatest._

class HelloWorldTests extends FunSuite {

  test("hello world") {
    val in = new URL("http://localhost:8080").openStream()

    val reader = new BufferedReader(new InputStreamReader(in))
    val out    = new StringBuilder()
    var line   = reader.readLine()
    while (line != null) {
      out.append(line)
      line = reader.readLine()
    }
    reader.close()
    assert("""<html><body><h1>Hello, world!</h1></body></html>""" === out.toString())
  }

}
