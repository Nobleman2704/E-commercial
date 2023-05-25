package com.example.ecommercial.domain.dto.request;

import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductUpdateRequest {
    private Long id;
    @Pattern(regexp = "^([a-zA-Z0-9]|\\s){4,30}$",
            message = "product name length should be at least 4 max 30 characters " +
                    "and it should only satisfy [a-zA-Z0-9] pattern")
    private String name;
    @Pattern(regexp = "^(\\w|\\s){1,500}$",
            message = "description should only satisfy [a-zA-Z0-9] pattern")
    private String description;
    private double price;
    private int amount;
}
