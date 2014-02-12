package com.earldouglas.xwptemplate

import scala.xml.NodeSeq
import javax.servlet.http.HttpServlet

class XwpTemplateServlet extends HttpServlet {

  import javax.servlet.http.HttpServletRequest
  import javax.servlet.http.HttpServletResponse

  override def doGet(request: HttpServletRequest, response: HttpServletResponse) {

    response.setContentType("text/html")
    response.setCharacterEncoding("UTF-8")

    val responseBody: NodeSeq =
       <html>
         <body>
           <h1>Hello, world!</h1>
           <p>{"21 + 21 = " + Math.add(21, 21).toString }</p>
         </body>
       </html>
    response.getWriter.write(responseBody.toString)
  }
}
