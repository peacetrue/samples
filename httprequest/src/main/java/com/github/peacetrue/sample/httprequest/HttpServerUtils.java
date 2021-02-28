package com.github.peacetrue.samples.httprequest;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

import static java.net.HttpURLConnection.HTTP_INTERNAL_ERROR;

/**
 * @author : xiayx
 * @since : 2020-12-29 15:01
 **/
public abstract class HttpServerUtils {

    static class TestHandler implements HttpHandler {
        public void handle(HttpExchange exchange) throws IOException {
            String response = "hello world";
            byte[] bytes = response.getBytes();
            exchange.sendResponseHeaders(HTTP_INTERNAL_ERROR, bytes.length);
            OutputStream os = exchange.getResponseBody();
            os.write(bytes);
            os.close();
        }
    }

    public static void start() throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8001), 0);
        server.createContext("/test", new TestHandler());
        server.start();
    }

}
