package com.earldouglas.xwptemplate

import javax.servlet.http.HttpServlet

class XwpTemplateServlet extends HttpServlet {

  import javax.servlet.http.HttpServletRequest
  import javax.servlet.http.HttpServletResponse
  import java.util.Scanner

  override def doPost(req: HttpServletRequest, res: HttpServletResponse) {

    def progress(pos: Int) =
      req.getContentLength match {
        case x if x >= 0 =>
          (pos.toDouble / x.toDouble * 100).toInt.toString + "%"
        case _ =>
          "(progress unknown)"
      }

    val s = new Scanner(req.getInputStream)
    s.useDelimiter("[\r\n]");

    while (s.hasNext) { // repeat until the request is completely processed

      val line = s.next // read a chunk from the request
      
      // simulate slow server-side processing of the chunk
      Thread.sleep(1000)

      // write the progress and the chunk to the response
      res.getWriter.write(progress(s.`match`.end) + ": " + line + "\n")

      // force the response buffer to be written immediately to the client
      res.flushBuffer

    }

  }
      
}
