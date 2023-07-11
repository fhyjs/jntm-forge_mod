package cn.fhyjs.jntm.utility;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class IHS {
    public void IHS() throws IOException {
        // 创建 http 服务器, 绑定本地 xxxx 端口
        HttpServer httpServer = HttpServer.create(new InetSocketAddress(8383), 0);
        httpServer.createContext("/", new HttpHandler() {
            @Override
            public void handle(HttpExchange httpExchange) throws IOException {
                System.out.println("url: " + httpExchange.getRequestURI().getQuery());
                httpExchange.sendResponseHeaders(200, "hello".length());
                httpExchange.getResponseBody().write("hello".getBytes());
            }
        });
        httpServer.start();
//            httpServer.stop(0);
    }
}
