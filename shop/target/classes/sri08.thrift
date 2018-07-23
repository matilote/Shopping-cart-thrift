namespace java edu.pjwstk.sri.lab08

struct Product {
  1: required i32 id,
  2: required string name,
  3: required double price,
  4: required i16 amount_of_stock
}

struct OrderItem {
  1: required Product product,
  2: required i16 amount
}

struct Basket {
  1: set<OrderItem> order_items,
  2: required i32 client_id,
  3: required i32 id
}

struct ShopClient {
  1: required i32 id,
  2: Basket basket,
  3: string name,
  4: string surname
}

exception ProductUnavailable {
  1: string message
}

exception ClientUnavailable {
  1: string message
}

service Store {
  list<Product> products_list(),
  Product find_product(1:i32 id) throws (1:ProductUnavailable unavailable),
  Basket create_basket(1:ShopClient client),
  ShopClient create_client(1:string name, 2:string surname),
  ShopClient find_client(1:i32 id) throws (1:ClientUnavailable unavailable),
  void add_product(1:string name, 2:double price, 3:i16 amount),
  void add_product_to_basket(1:Product product, 2:i16 amount, 3:i32 client_id) throws (1:ProductUnavailable unavailable),
  void remove_product_from_basket(1:Product product, 2:i32 client_id) throws (1:ProductUnavailable unavailable),
  void change_product_amount_from_basket(1:Product product, 2:i16 amount, 3:i32 client_id) throws (1:ProductUnavailable unavailable),
  i32 checkout(1:i32 client_id) throws (1:ProductUnavailable unavailable)
}
