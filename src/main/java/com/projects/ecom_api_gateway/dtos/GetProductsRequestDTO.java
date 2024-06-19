package com.projects.ecom_api_gateway.dtos;

import lombok.Data;

import java.util.List;

@Data
public class GetProductsRequestDTO {
    private List<Long> productIds;
}
