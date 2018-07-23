package edu.pjwtk.sri.lab08.server;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.thrift.TException;

import edu.pjwtk.sri.lab08.Basket;
import edu.pjwtk.sri.lab08.ClientUnavailable;
import edu.pjwtk.sri.lab08.OrderItem;
import edu.pjwtk.sri.lab08.Product;
import edu.pjwtk.sri.lab08.ShopClient;
import edu.pjwtk.sri.lab08.ProductUnavailable;
import edu.pjwtk.sri.lab08.Store;

public class ProductCartHandler implements Store.Iface {

	
	 private List<Product> productList = new ArrayList<>();
	 private List<ShopClient> clientList = new ArrayList<>();
	 private List<Basket> basketList = new ArrayList<>();
	 private static int idfree = 0;

	@Override
	public List<Product> products_list() throws TException {
		return this.productList;
	}
	
	@Override
	public void add_product(String name, double price, short amount) throws TException {
		Product p = new Product();
		p.id = idfree;
		idfree = idfree +1;
		p.name = name;
		p.price = price;
		p.amount_of_stock = amount;
		productList.add(p);	
	}


	@Override
	public Product find_product(int id) throws ProductUnavailable, TException {
		for(Product product : productList) {
			if(product.id == id) {
				return product;
			} 
		}
		throw new ProductUnavailable("Product not found, try again...");
	}


	@Override
	public void add_product_to_basket(Product product, short amount, int client_id)
			throws ProductUnavailable, TException {
		product = find_product(product.id);
		if (product.amount_of_stock >= amount) {
			System.out.println(product.amount_of_stock -= amount);
			OrderItem order_item = new OrderItem();
			order_item.product = product;
			order_item.amount = amount;
			ShopClient client = find_client(client_id);
			System.out.println(client);
			client.basket = add_order_item_to_basket(client.basket, order_item);
		} else {
			
			throw new ProductUnavailable("Not enough products in the basket");
		}
		
	}

	@Override
	public void remove_product_from_basket(Product product, int client_id)
			throws ProductUnavailable, TException {
		Basket basket = find_basket_by_client_id(client_id);
		Product product1 = find_product(product.id);
		for(OrderItem order_item : basket.order_items) {
			System.out.println("asdasdasdasd22222");
			if(order_item.product.equals(product1)) {
				System.out.println("xxxxxxxxxxxxxxxxxxxxx");
				System.out.println(basket.order_items);
				System.out.println(basket.order_items.remove(order_item));
				product1.amount_of_stock += order_item.amount;
				System.out.println(basket.order_items);
			} else {
				throw new ProductUnavailable("There is no such product in the basket");
			}
		}		
	}
	

	public Basket find_basket_by_client_id(int id) {
		for(Basket basket : basketList) {
			System.out.println(basket);
			if(basket.id == id) {return basket;}
		}
		return null;
	}
	

	public Basket add_order_item_to_basket(Basket basket, OrderItem order_item) {
		if (basket.order_items != null ) {
			basket.order_items.add(order_item);
		} else {
			basket.order_items = new HashSet<>();
			basket.order_items.add(order_item);
		}
		return basket; 
	}

	@Override
	public void change_product_amount_from_basket(Product product, short amount, int client_id)
			throws ProductUnavailable, TException {
		Basket basket = find_basket_by_client_id(client_id);
		Product product1 = find_product(product.id);
		System.out.println("asdadasdasda");
		System.out.println(basket.order_items);
		for(OrderItem order_item : basket.order_items) {
			System.out.println(order_item.product);
			if(order_item.product.equals(product1)) {
				product1.amount_of_stock += order_item.amount;
//			} else {
				if( product1.amount_of_stock < amount) {
					throw new ProductUnavailable("Not enough products in the basket");
				}
			product1.amount_of_stock -= amount;
			order_item.amount = amount;
			}
		}		
	}

	@Override
	public int checkout(int client_id) throws ProductUnavailable, TException {
		System.out.println("ELO");
		ShopClient client = find_client(client_id);
		Basket basket = client.basket;
		int totalAmount = 0;		
		for(OrderItem order_item : basket.order_items) {
			System.out.println(find_product(order_item.product.id));
			if(find_product(order_item.product.id).amount_of_stock >= order_item.amount) {
				totalAmount += order_item.product.price * order_item.amount;
			} else {
				throw new ProductUnavailable("Not enough products in the basket");
			}
		}
		return totalAmount;
	}

	@Override
	public void cancel_order(int client_id) throws TException {
		Basket basket = find_basket_by_client_id(client_id);
		for(OrderItem order_item : basket.order_items) {
			Product product = find_product(order_item.product.id);
			product.amount_of_stock += order_item.amount;
		}
		new HashSet<OrderItem>(basket.order_items);
	}
	
	@Override
	public ShopClient find_client(int id) throws ClientUnavailable, TException {
		for(ShopClient client : clientList) {
			if(client.id == id) {
				return client;
			} 
		}
		throw new ClientUnavailable("Client not found, try again...");
	}

	@Override
	public Basket create_basket(ShopClient client) throws TException {
		Basket basket = new Basket();
		if (basketList.size() > 0) {
			basket.id = basketList.lastIndexOf(basket) + 1;
		} else {
			basket.id = 0;
		}
		basket.client_id = client.id;
		client.basket = basket;
		basketList.add(basket);
		return basket;
	}
	

	@Override
	public ShopClient create_client(String name, String surname) throws TException {
		ShopClient client = new ShopClient();
		if (clientList.size() > 0) {	
		client.id = clientList.lastIndexOf(client) + 1;
		} else {
			client.id = 0;
		}
		client.name = name;
		client.surname = surname;
		clientList.add(client);
		create_basket(client);
		return client;
		
	}

}
