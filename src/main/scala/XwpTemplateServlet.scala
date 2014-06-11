package com.earldouglas.xwptemplate

import javax.servlet.http.HttpServlet

class XwpTemplateServlet extends HttpServlet {

  import javax.servlet.http.HttpServletRequest
  import javax.servlet.http.HttpServletResponse

  override def doGet(req: HttpServletRequest, res: HttpServletResponse) {
    res.sendRedirect("/")
  }

  override def doPost(req: HttpServletRequest, res: HttpServletResponse) {

    val file = req.getPart("file")

    var index = 0L
    var buffer = Array.fill[Byte](1024)(0)
    var lastProgress = 0

    val stream = file.getInputStream
    while (stream.available > 0) { // repeat until the request is processed

      val read = stream.read(buffer) // read a chunk from the request
      Thread.sleep(10) // simulate slow server-side processing of the chunk

      index = index + read // track progress

      val progress = (index.toDouble / file.getSize.toDouble * 100).toInt

      if (progress > lastProgress) {
        // write progress to the response
        res.getWriter.write(progress.toString + "%" + "\n")
        lastProgress = progress
      }

      res.flushBuffer // force-write the response buffer to the client

    }

  }
      
}
