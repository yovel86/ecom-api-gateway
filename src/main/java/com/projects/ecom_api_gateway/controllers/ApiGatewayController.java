package com.projects.ecom_api_gateway.controllers;

import com.projects.ecom_api_gateway.dtos.GetDashboardRequestDTO;
import com.projects.ecom_api_gateway.dtos.GetProductsRequestDTO;
import com.projects.ecom_api_gateway.dtos.ListResponse;
import com.projects.ecom_api_gateway.dtos.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@RestController
public class ApiGatewayController {

    private final WebClient webClient;

    @Autowired
    public ApiGatewayController(WebClient webClient) {
        this.webClient = webClient;
    }

    @GetMapping("/api/dashboard")
    public Mono<Map<String, Object>> getDashboard(@RequestBody GetDashboardRequestDTO requestDTO) {
        long userId = requestDTO.getUserId();
        Mono<UserDTO> userResponse = this.webClient.get()
                                            .uri("http://localhost:8085/users/profile/" + userId)
                                            .retrieve()
                                            .bodyToMono(UserDTO.class);
        Mono<ListResponse> trendingProducts = this.webClient.get()
                                            .uri("http://localhost:8082/orders/trending")
                                            .retrieve()
                                            .bodyToFlux(Long.class).collectList()
                                            .flatMap(this::getTrendingProducts);
        return Mono.zip(userResponse, trendingProducts).map(tuple -> Map.of("user", tuple.getT1(), "trendingProducts", tuple.getT2()));
    }

    private Mono<ListResponse> getTrendingProducts(List<Long> productIds) {
        GetProductsRequestDTO requestDTO = new GetProductsRequestDTO();
        requestDTO.setProductIds(productIds);
        return this.webClient.post()
                .uri("http://localhost:8081/products/details")
                .body(BodyInserters.fromValue(requestDTO))
                .retrieve().bodyToMono(ListResponse.class);
    }

}
