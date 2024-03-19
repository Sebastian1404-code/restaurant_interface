package com.example.final_project_fxml;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RestaurantController {

        private SharedController sharedController;

        public void setSharedController(SharedController sharedController) {
            this.sharedController = sharedController;
        }
        @FXML
        private MenuBar menuBar;

        @FXML
        private GridPane menu_items;

        @FXML
        private Button cash;

        @FXML
        private Button card;

        @FXML
        private Label price;

        @FXML
        private TableView<Product> table;

        @FXML
        private TableColumn<Product, String> productName;
        @FXML
        private TableColumn<Product, Integer> units;
        @FXML
        private TableColumn<Product, Integer> total;




        @FXML
        protected void initialize() throws SQLException {

            table.setEditable(true);
            menu_items.setPadding(new Insets(5));
            menu_items.setVgap(3);
            menu_items.setHgap(3);

            productName.setCellValueFactory(new PropertyValueFactory<Product, String>("productName"));
            units.setCellValueFactory(new PropertyValueFactory<Product, Integer>("units"));
            total.setCellValueFactory(new PropertyValueFactory<Product, Integer>("total"));

            table.refresh();// verify here
            doubleClick();

            Connection conn = Connection_db.connect_db("restaurant", "postgres", "Sebi1404");
            ResultSet categories = Connection_db.read_categories(conn, "category");
            if (categories != null) {
                while (categories.next()) {
                    int id = categories.getInt("id");
                    Menu menu = new Menu();
                    menu.setText(categories.getString("name"));
                    menuBar.getMenus().add(menu);
                    ResultSet subcategories = Connection_db.read_subcategories(conn, "subcategory", id);
                    if (subcategories != null) {
                        while (subcategories.next()) {
                            int id_subcategory = subcategories.getInt("id");
                            MenuItem menuItem = new MenuItem();
                            menuItem.setText(subcategories.getString("name"));
                            menu.getItems().add(menuItem);
                            menuItem.setOnAction(e -> {
                                menu_items.getChildren().clear();
                                ResultSet products = Connection_db.read_product(conn, "product", id_subcategory);
                                if (products != null) {
                                    int i = 0, j = 0;


                                    try {
                                        while (products.next()) {
                                            int ProductId = products.getInt("id");
                                            Button btn = new Button();
                                            btn.setStyle("-fx-background-color:  #4287f5");
                                            btn.setPrefSize(160, 62);
                                            String productName = products.getString("name");
                                            int price = products.getInt("price");

                                            btn.setOnAction(
                                                    event -> {
                                                        try {
                                                            update(ProductId);
                                                        } catch (SQLException ex) {
                                                            throw new RuntimeException(ex);
                                                        }

                                                    }
                                            );


                                            btn.setText(productName + " \n      " + price + " lei");
                                            System.out.println(btn.getText());

                                            GridPane.setConstraints(btn, j, i);
                                            j++;
                                            if (j == 2) {
                                                j = 0;
                                                i++;
                                            }
                                            menu_items.getChildren().add(btn);


                                        }


                                    } catch (SQLException exception) {
                                        exception.printStackTrace();
                                    }

                                }

                            });

                        }
                    }
                }

            }
            //fetchAndDisplayAllOrders();

        }
    void fetchAndDisplayAllOrders() throws SQLException {
        ObservableList<Product> productList = FXCollections.observableArrayList();
        Button lastClickedButton = sharedController.getLastClickedButton();
        String fakeId = lastClickedButton.getId();
        Integer btnId = (int) fakeId.charAt(1) - 48;
        System.out.println(btnId);
        int total_price = 0;

        Connection conn = Connection_db.connect_db("restaurant", "postgres", "Sebi1404");
        ResultSet resultSet = Connection_db.findAllOrders(conn, btnId);

        while (resultSet.next()) {
            int interm = resultSet.getInt("units") * resultSet.getInt("price");
            total_price += interm;
            Product product = new Product(resultSet.getString("name"), resultSet.getInt("units"), interm);
            productList.add(product);
        }

        // Set the total price label
        price.setText("Total: " + total_price);

        // Set the items in the table
        table.setItems(productList);
    }

        protected void update(int ProductId) throws SQLException {
            ObservableList<Product> productList = FXCollections.observableArrayList();
            Button lastClickedButton = sharedController.getLastClickedButton();
            String fakeId=lastClickedButton.getId();
            Integer btnId= (int) fakeId.charAt(1)-48;
            System.out.println(btnId);
            int total_price=0;

            Connection conn = Connection_db.connect_db("restaurant", "postgres", "Sebi1404");
            ResultSet res=Connection_db.findAllOrdersById(conn,btnId,ProductId);
            if(!res.next())
            {
                Connection_db.insert_order(conn,"order",btnId,1,ProductId);
            }
            else
            {
                int units=res.getInt("units");
                int Id=res.getInt("id");
                Connection_db.update_units(conn,"order",units,Id);
            }
            ResultSet resultSet=Connection_db.findAllOrders(conn,btnId);
            while(resultSet.next())
            {
                int interm=resultSet.getInt("units")*resultSet.getInt("price");
                total_price+=interm;
                Product product=new Product(resultSet.getString("name"),resultSet.getInt("units"),interm);
                productList.add(product);
            }
            price.setText("Total: "+total_price);
            table.setItems(productList);

        }
        protected void deleteOrder(int table_id)
        {
            Connection conn = Connection_db.connect_db("restaurant", "postgres", "Sebi1404");
            Connection_db.deleteOrderById(conn,table_id);

        }


    private void switchScene(String fxmlFile,Button button) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root=fxmlLoader.load();

          /*  RestaurantController restaurantController = fxmlLoader.getController();
            restaurantController.setSharedController(sharedController);*/
            Stage newStage=new Stage();
            newStage.setScene(new Scene(root));
            newStage.show();

            Stage currentStage=(Stage) button.getScene().getWindow();
            currentStage.close();

        }
        catch(IOException e)
        {
            e.printStackTrace();

        }
    }

    @FXML
    protected void payment_cash(ActionEvent event)
    {
        Button btn = (Button) event.getSource();
        Button lastClickedButton = sharedController.getLastClickedButton();
        String fakeId=lastClickedButton.getId();
        Integer btnId= (int) fakeId.charAt(1)-48;
        System.out.println(btnId);
        deleteOrder(btnId);
        System.out.println("Cash payment");
        switchScene("home.fxml",btn);
    }

    @FXML
    protected void payment_card(ActionEvent event)
    {
        Button btn = (Button) event.getSource();
        Button lastClickedButton = sharedController.getLastClickedButton();
        String fakeId=lastClickedButton.getId();
        Integer btnId= (int) fakeId.charAt(1)-48;
        deleteOrder(btnId);
        System.out.println("Card payment");
        switchScene("home.fxml",btn);
    }

    @FXML
    protected void back_button(ActionEvent event)
    {
        Button btn = (Button) event.getSource();
        switchScene("home.fxml",btn);
    }


    @FXML
    protected void doubleClick()
    {

        table.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2) {
                // Get the selected item on double-click
                Product selectedItem = table.getSelectionModel().getSelectedItem();
                Button lastClickedButton = sharedController.getLastClickedButton();
                String fakeId=lastClickedButton.getId();
                Integer btnId= (int) fakeId.charAt(1)-48;

                Connection conn=Connection_db.connect_db("restaurant", "postgres", "Sebi1404");
                ResultSet res=Connection_db.findId(conn,btnId,selectedItem.getProductName());
                try {
                    res.next();
                    if(selectedItem.getUnits()>1) {
                        Connection_db.update_units(conn, "order", res.getInt("units") - 2, res.getInt("id"));
                        table.refresh();
                    }
                    else {
                        System.out.println("here");
                        Connection_db.deleteOrderByName(conn,btnId,selectedItem.getProductName());
                    }
                    fetchAndDisplayAllOrders();

                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("Double-clicked on: " + selectedItem.getProductName());
            }
        });
    }
    /*protected void refresh_table()
    {
        Connection conn = Connection_db.connect_db("restaurant", "postgres", "Sebi1404");
        ResultSet res=Connection_db.findAllOrdersById(conn,btnId,ProductId);
        if(!res.next())
        {
            Connection_db.insert_order(conn,"order",btnId,1,ProductId);
        }
        else
        {
            int units=res.getInt("units");
            int Id=res.getInt("id");
            Connection_db.update_units(conn,"order",units,Id);
        }
        ResultSet resultSet=Connection_db.findAllOrders(conn,btnId);
        while(resultSet.next())
        {
            int interm=resultSet.getInt("units")*resultSet.getInt("price");
            total_price+=interm;
            Product product=new Product(resultSet.getString("name"),resultSet.getInt("units"),interm);
            productList.add(product);
        }
        price.setText("Total: "+total_price);
        table.setItems(productList);

    }*/

}

