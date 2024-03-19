package com.example.final_project_fxml;


public class Product {
    private String productName;
    private Integer units;
    private Integer total;

    public Product(String productName, Integer units, Integer total) {
        this.productName = productName;
        this.units = units;
        this.total = total;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setUnits(Integer units) {
        this.units = units;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public String getProductName() {
        return productName;
    }

    public Integer getUnits() {
        return units;
    }

    public Integer getTotal() {
        return total;
    }
}
