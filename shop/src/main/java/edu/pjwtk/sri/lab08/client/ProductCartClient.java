package edu.pjwtk.sri.lab08.client;

import java.util.ArrayList;
import java.util.List;

import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;

import edu.pjwtk.sri.lab08.OrderItem;
import edu.pjwtk.sri.lab08.Product;
import edu.pjwtk.sri.lab08.ProductUnavailable;
import edu.pjwtk.sri.lab08.ShopClient;
import edu.pjwtk.sri.lab08.Store;
import edu.pjwtk.sri.lab08.Store.Client;
import edu.pjwtk.sri.lab08.Store.Processor.products_list;
import edu.pjwtk.sri.lab08.server.ProductCartHandler;

public class ProductCartClient {
		  
	  public static void main(String [] args) {

		   
		    try {
		      TTransport transport;
		     
		      transport = new TSocket("localhost", 8000);
		      transport.open();

		      TProtocol protocol = new  TBinaryProtocol(transport);
		      Store.Client client = new Store.Client(protocol);

		      perform(client);

		      transport.close();
		    } catch (TException x) {
		      x.printStackTrace();
		    } 
		  }
	 

		  private static void perform(Store.Client client) throws TException
		  {		
 
	
		    System.out.println("Welcome to Thrift Shop!");
		    System.out.println(client.create_client("John", "Kowalski"));
		    client.add_product("sadasd", 11, (short) 200);
			System.out.println(client.products_list());
			Product first_product = client.find_product(0);
//			Product second_product = client.find_product(2);
			client.add_product_to_basket(first_product, (short) 1, 0);
//			client.add_product_to_basket(second_product, (short) 2, 2);
			System.out.println(first_product.amount_of_stock);
//			System.out.println(second_product.amount_of_stock);
			System.out.println(client.find_client(0));
			System.out.println("Removing product from cart");
			client.remove_product_from_basket(first_product, 0);
			System.out.println(client.checkout(0));
			System.out.println("Change product amount");
			client.change_product_amount_from_basket(first_product, (short) 20, 0);
			System.out.println(client.find_product(0));
			System.out.println(client.checkout(0));
		  }

	}

