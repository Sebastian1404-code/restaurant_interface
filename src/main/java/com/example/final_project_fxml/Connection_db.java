package com.example.final_project_fxml;
import java.sql.*;


public class Connection_db {
    public static Connection connect_db(String dbname,String user,String pass)
    {
        Connection conn=null;

        try {
            Class.forName("org.postgresql.Driver");
            conn=DriverManager.getConnection("jdbc:postgresql://localhost:5432/"+dbname,user,pass);
            if(conn!=null)
            {
                System.out.println("yes");
            }
            else {
                System.out.println("Fail");
            }
        } catch (Exception e) {
            System.out.println("error");
            System.out.println(e);
        }
        return conn;
    }
    public static String read_pass(Connection conn,String table,String user)
    {
        try (PreparedStatement statement = conn.prepareStatement("SELECT pass FROM "+ table +" l WHERE l.user = ?")) {
            statement.setString(1, user); // Set the value for the parameter


            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    return result.getString("pass");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Log or handle the exception appropriately
        }
        return "0";
    }

    /*public static ResultSet read_users(Connection conn,String table)
    {
        Statement statement;
        ResultSet result;
        try
        {
            String query=String.format("Select * from %s",table);
            statement=conn.createStatement();
            result=statement.executeQuery(query);
            return result;

        }
        catch (Exception e)
        {
            System.out.println("fail");
        }
        return null;
    }
*/
    public static ResultSet read_categories(Connection conn,String table)
    {
        Statement statement;
        ResultSet result;
        try
        {
            String query=String.format("Select * from %s",table);
            statement=conn.createStatement();
            result=statement.executeQuery(query);
            return result;

        }
        catch (Exception e)
        {
            System.out.println("fail");
        }
        return null;
    }
    public static ResultSet read_subcategories(Connection conn,String table,int id)
    {
        Statement statement;
        ResultSet result;
        try
        {
            String query=String.format("Select * from %s where id_category=%d",table,id);
            statement=conn.createStatement();
            result=statement.executeQuery(query);
            return result;

        }
        catch (Exception e)
        {
            System.out.println("fail");
        }
        return null;
    }

    public static ResultSet read_product(Connection conn,String table,int id)
    {
        Statement statement;
        ResultSet result;
        try
        {
            String query=String.format("Select * from %s where id_subcategory=%d",table,id);
            statement=conn.createStatement();
            result=statement.executeQuery(query);
            return result;

        }
        catch (Exception e)
        {
            System.out.println("faileeed");
        }
        return null;
    }

    public static void insert_order(Connection conn,String table,int table_id,int units,int product_id)
    {
        Statement statement;
        try
        {
            String query=String.format("Insert into \"%s\" (table_id,units,product_id) values (%d,%d,%d)",table,table_id,units,product_id);
            statement=conn.createStatement();
            statement.executeUpdate(query);
            //return result;

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        //return null;
    }

    public static void update_units(Connection conn,String table,int units,int id)
    {
        Statement statement;
        ResultSet result;
        try
        {
            String query=String.format("Update \"%s\" set units=%d where id=%d",table,units+1,id);
            statement=conn.createStatement();
            statement.executeUpdate(query);

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    /*public static ResultSet findOrder(Connection conn,int product_id)
    {
        Statement statement;
        ResultSet result;
        try
        {
            String query=String.format("select * from \"order\" o join \"tables\" t on t.id =o.table_id join product p on p.id =o.product_id where product_id=%d",product_id);
            statement=conn.createStatement();
            result=statement.executeQuery(query);
            return  result;

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }*/

    public static ResultSet findAllOrdersById(Connection conn,int table_id,int ProductId)
    {
        Statement statement;
        ResultSet result;
        try
        {
            String query=String.format("select * from \"order\" o join \"tables\" t on t.id =o.table_id join product p on p.id=o.product_id where table_id=%d and p.id=%d",table_id,ProductId);
            statement=conn.createStatement();
            result=statement.executeQuery(query);
            return  result;

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }


    public static ResultSet findAllOrders(Connection conn,int table_id)
    {
        Statement statement;
        ResultSet result;
        try
        {
            String query=String.format("select p.name,o.units,p.price from \"order\" o join \"tables\" t on t.id =o.table_id join product p on p.id=o.product_id where table_id=%d",table_id);
            statement=conn.createStatement();
            result=statement.executeQuery(query);
            return  result;

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public static ResultSet findId(Connection conn,int table_id,String name)
    {
        Statement statement;
        ResultSet result;
        try
        {
            String query=String.format("select o.id,o.units from \"order\" o join \"tables\" t on t.id =o.table_id join product p on p.id=o.product_id where table_id=%d and p.name='%s'",table_id,name);
            System.out.println(query);
            statement=conn.createStatement();
            result=statement.executeQuery(query);
            return  result;

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }



    public static void deleteOrderById(Connection conn,int table_id) {

        try {
            Statement statement = conn.createStatement();

            // Execute the DELETE statement to remove all rows from the table
            String deleteQuery = String.format("DELETE FROM \"order\" o where o.table_id=%d",table_id);
            int rowsAffected = statement.executeUpdate(deleteQuery);

            // Display the number of rows affected

            // Close resources
            statement.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteOrderByName(Connection conn,int table_id,String name) {

        try {
            Statement statement = conn.createStatement();

            // Execute the DELETE statement to remove all rows from the table
            String deleteQuery = String.format("DELETE FROM \"order\" o using \"tables\" t, product p where t.id =o.table_id and p.id=o.product_id and table_id=%d and p.name='%s'",table_id,name);
            System.out.println(deleteQuery);
            int rowsAffected = statement.executeUpdate(deleteQuery);

            // Display the number of rows affected

            // Close resources
            statement.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
