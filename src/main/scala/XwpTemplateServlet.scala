package com.earldouglas.xwptemplate

import javax.servlet.http.HttpServlet

class XwpTemplateServlet extends HttpServlet {

  import javax.servlet.http.HttpServletRequest
  import javax.servlet.http.HttpServletResponse

  override def service(req: HttpServletRequest, res: HttpServletResponse) {

    val r = req.getReader

    def echoLines: Unit =
    Option(r.readLine) foreach { line => // read a chunk from the request

      // simulate slow server-side processing
      Thread.sleep(1000)

      // write a chunk to the response
      res.getWriter.write("read line: " + line + "\n")

      // force the response buffer to be written to the client
      res.flushBuffer

      // repeat until the request is completely processed
      echoLines
    }

    echoLines
  }
      
}
