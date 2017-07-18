package com.test;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
public class BuyXGetYOffer implements Offer {
    public static final BigDecimal ZERO = new BigDecimal("0.0");
    private int x;
    private int y;

    @Override
    public void apply(Product product, List<Product> products, Map<Product, BigDecimal> discountByProduct) {
        int noOfProducts = products.stream()
                .filter(p -> p.getId() == product.getId())
                .collect(Collectors.toList())
                .size();
        if(noOfProducts >= x+y) {
            BigDecimal calculatedDiscount = product.getPrice();
            discountByProduct.put(product,
                    discountByProduct.getOrDefault(discountByProduct, ZERO)
                            .add(calculatedDiscount));
        }
    }
}
