package com.projects.ecom_api_gateway.dtos;

import lombok.Data;

@Data
public class Product {
    private long id;
    private String title;
    private String description;
    private double price;
    private String image;
    private int availableQuantity;
    private Category category;
}
