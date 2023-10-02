package Connection;

import Models.Orders;
import Models.Products;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class AccessToProduct implements ConnectionPool, getProductsList, getOrdersList, getOrderLinesList {
    public void getProductsAndPrint(int id) throws SQLException{

        int order_id;
        String item_name;
        String item_color;


        List<Integer> ordersID = new ArrayList<>();
        getOrdersList().forEach(x -> ordersID.add(x.getId()));
        if(ordersID.contains(id)) {

            System.out.println("The list of products with id # " + id + ":");


            try(Connection con = ConnectionPool.getConnection()) {
                Statement stm = con.createStatement();
                ResultSet rs = stm.executeQuery("SELECT order_lines.order_id, item_name, item_color FROM products INNER JOIN order_lines ON order_id = " + id + " AND order_lines.item = products.item");
                    while (rs.next()) {
                        order_id = rs.getInt(1);
                        item_name = rs.getString(2);
                        item_color = rs.getString(3);
                        System.out.println("order id: " + order_id + ", item: " + item_name + ", color: " + (item_color != null ? item_color : "the color is not defined."));

                    }
                }

            catch(SQLException se){
                System.out.println(se);
            }
        }
        else
            System.out.println("The order id " + id + " does not exist in the database");
    }


    public void newOrder(String customer_name, String phone_num, String email,
                         String delivery_address, Map<String, Integer> basket) throws SQLException   {

        int newOrderNumber = lastOrderNumber() + 1;
        String sqlFirst = "INSERT INTO orders VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        String sqlSecond = "INSERT INTO order_lines VALUES (?, ?, ?, ?)";

        try (Connection con = ConnectionPool.getConnection()){
            PreparedStatement psA = con.prepareStatement(sqlFirst);
            psA.setInt(1, newOrderNumber);
            psA.setDate(2, Date.valueOf(LocalDate.now()));
            psA.setString(3, customer_name);
            psA.setString(4, phone_num);
            psA.setString(5, email);
            psA.setString(6, delivery_address);
            psA.setString(7, "P");
            psA.setDate(8, null);
            psA.executeUpdate();
        } catch(SQLException se){
            System.out.println(se.toString());
        }

        basket.forEach((x, y) -> {
            try (Connection con = ConnectionPool.getConnection()){
                PreparedStatement psB = con.prepareStatement(sqlSecond);
                psB.setInt(1, newOrderNumber);
                psB.setString(2, x);
                psB.setInt(3, getPrice("'"+x+"'"));
                psB.setInt(4, y);

                psB.executeUpdate();
            }
            catch(SQLException se){
                System.out.println("An exception has been thrown");
            }
        });
        System.out.println("The order № " + newOrderNumber + " has been successfully placed");
    }

    public int lastOrderNumber() throws SQLException{
        List<Orders> ordersList = getOrdersList();
        List<Integer> orderNumbers = new ArrayList<>();
        ordersList.forEach(x -> orderNumbers.add(x.getId()));
        Collections.sort(orderNumbers);
        return orderNumbers.get(orderNumbers.size()-1);
    }
    public int getPrice(String item) throws SQLException{
        int price = 0;
        try(Connection con = ConnectionPool.getConnection()){

            Statement stm = con.createStatement();
            ResultSet rs = stm.executeQuery("SELECT item_price FROM products WHERE item = " + item);
            while(rs.next()){
                price = rs.getInt(1);
            }
        } catch(SQLException se){
            se.printStackTrace();
        }
        return price;
    }

}




