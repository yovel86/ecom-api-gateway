package com.projects.ecom_api_gateway.dtos;

import lombok.Data;

import java.util.List;

@Data
public class ListResponse {
    private List<Product> products;
    private String message;
    private ResponseStatus responseStatus;
}
