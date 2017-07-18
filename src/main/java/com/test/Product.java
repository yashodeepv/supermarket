package com.test;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @NonNull
    private Long id;

    @NonNull
    private String name;

    private LocalDate manufacturingDate;

    @NonNull
    private BigDecimal price;

    private Offer offer;

}
