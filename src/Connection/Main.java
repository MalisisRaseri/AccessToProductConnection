package Connection;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws SQLException {

        AccessToProduct base = new AccessToProduct();
        base.getProductsList().forEach(System.out::println);
        System.out.println();
        base.getOrdersList().forEach(System.out::println);
        System.out.println();
        base.getOrderLinesList().forEach(System.out::println);
        System.out.println();

        base.getProductsAndPrint(3);
        base.getProductsAndPrint(4);


        Map<String, Integer> basket1 = new HashMap<>();

        basket1.put("3251615", 2);
        basket1.put("3251617", 3);
        basket1.put("3251619", 4);

        Map<String, Integer> basket2 = new HashMap<>();

        basket2.put("3251619", 45);

        base.newOrder("Иванова Татьяна Васильевна", "8800512", "ivaova@mail.ru", "Санкт-Петербург, ул. Радищева", basket1);
        base.newOrder("Иванова Татьяна Васильевна", "8800512", "ivaova@mail.ru", "Санкт-Петербург, ул. Радищева", basket2);





    }
}

