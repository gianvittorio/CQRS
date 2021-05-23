package com.gianvittorio.estore.OrdersService.OrdersService.command.rest.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Data
@Builder
public class CreateOrderRestModel {

    @NotBlank
    private String productId;

    @Min(value = 1, message = "Quantity cannot be lower than 1")
    @Max(value = 5, message = "Quantity cannot be greater than 5")
    private Integer quantity;

    @NotBlank
    private String addressId;
}
