package com.fredhopper.lifecycle;

import java.io.IOException;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpServer;

class TestHttpServer extends AbstractLifeCycle {

  private HttpServer server;

  TestHttpServer() {}

  @Override
  protected void doInitLifeCycle() throws Exception {
    try {
      this.server = HttpServer.create(new InetSocketAddress(0), 2);
    } catch (IOException e) {
      throw new Exception(e);
    }
  }

  @Override
  protected void doStartLifeCycle() throws Exception {
    this.server.start();
  }

  @Override
  protected void doStopLifeCycle() throws Exception {
    this.server.stop(0);
  }

  HttpServer getServer() {
    return server;
  }

}
