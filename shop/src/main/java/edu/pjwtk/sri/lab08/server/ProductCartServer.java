package edu.pjwtk.sri.lab08.server;

import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TServer.Args;
import org.apache.thrift.server.TSimpleServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;

import edu.pjwtk.sri.lab08.Store;

// Generated code

public class ProductCartServer {

	  public static ProductCartHandler handler;

	  public static Store.Processor processor;

	  public static void main(String [] args) {
	    try {
	      handler = new ProductCartHandler();
	      processor = new Store.Processor(handler);

	      Runnable simple = new Runnable() {
	        public void run() {
	          simple(processor);
	        }
	      };      
	     
	      new Thread(simple).start();
	    } catch (Exception x) {
	      x.printStackTrace();
	    }
	  }

	  public static void simple(Store.Processor processor) {
	    try {
	      TServerTransport serverTransport = new TServerSocket(8000);
	      TServer server = new TSimpleServer(new Args(serverTransport).processor(processor));
//	      TServer server = new TThreadPoolServer(new TThreadPoolServer.Args(serverTransport).processor(processor));
	      System.out.println("Starting the simple server...");
	      server.serve();
	    } catch (Exception e) {
	      e.printStackTrace();
	    }
	  } 
}