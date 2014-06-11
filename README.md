# Chunked HTTP with Ajax and Scala

*April 16, 2014*

In Web programming, it is often handy to be able to process an HTTP request as 
a stream while sending back periodic status updates to the client.  Consider an 
application that performs some time-consuming computation on incremental subsets 
of request data, or an application that handles the uploading of a large file.

Using [Java servlets](http://en.wikipedia.org/wiki/Java_Servlet), we can write 
a simple server that reads a file from a multipart POST request, processes the
file slowly, and keeps the client updated with the current progress:

```scala
def doPost(req: HttpServletRequest, res: HttpServletResponse) {

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
```

We can test this service using curl with the `--no-buffer` option, observing 
the return of each incremental percentage point:

```
$ curl --no-buffer -X POST -F file=@test.bmp localhost:8080/upload
1%
2%
3%
4%
...
```

To interact with this service from JavaScript, we need an `XMLHttpRequest`:

```javascript
var xhr = new XMLHttpRequest();
```

We also need to be able to do something with the output, so let's make a
progress bar:

```html
<style>
  div.progress { border: solid 1px; width: 360px; height: 14px; }
  div.bar      { background-color: #3cf; height: 14px; } 
  div.label    { margin-top: 6px; float: right; }
</style>
```

```html
<div class="progress">
  <div id="bar" class="bar" style="width: 0%">
    &nbsp;
  </div>
  <div id="label" class="label">
    &nbsp;
  </div>
</div>
```

Finally, we can make a request and handle the response.  We're watching for a 
`readyState` of `3`, which means [*processing request*](http://www.w3schools.com/ajax/ajax_xmlhttprequest_onreadystatechange.asp):

```javascript
function upload() {
  var file = document.getElementById('file').files[0];
  var formData = new FormData();
  formData.append('file', file, file.name);

  var xhr = new XMLHttpRequest();
  xhr.open("post", "/upload", true);
  xhr.onreadystatechange =
    function() {
      if (xhr.readyState == 3 && xhr.status == 200) {
        var chunks = xhr.responseText.split('\n');
        var chunk = chunks[chunks.length - 2];
        document.getElementById('bar').style.width = chunk;
        document.getElementById('label').innerHTML = chunk;
      }
      if (xhr.readyState == 4 && xhr.status == 200) {
        document.getElementById('label').innerHTML = "Done!";
      }
    };
  xhr.send(formData);
}
```

Connecting this to an `input` of type `file`, we get something like this:

![chunky-http screenshot](https://raw.githubusercontent.com/earldouglas/xwp-template/chunky/readme/chunky-http.png)

Check out the demo at [chunky-http.herokuapp.com](http://chunky-http.herokuapp.com/)

This article was inspired by [*Servlet/Javascript chunking*](http://stackoverflow.com/questions/23095434/servlet-javascript-chunking/23113819) 
on Stack Overflow.
