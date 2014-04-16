This is an example of HTTP request/response streaming/chunking.

To test from the console, use the `-N` option with curl:

```bash
curl -N -X POST --data-binary @test.txt localhost:8080/echo
```

To test from a browser, head to [localhost:8080](http://localhost:8080/)
