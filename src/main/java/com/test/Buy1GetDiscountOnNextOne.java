package com.test;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
public class Buy1GetDiscountOnNextOne implements Offer {

    private static final BigDecimal ZERO = new BigDecimal("0.0");
    private static final BigDecimal DIVISOR = new BigDecimal("100");

    private BigDecimal discount;

    public Buy1GetDiscountOnNextOne(int discountPercent) {
        this.discount = new BigDecimal(String.valueOf(discountPercent)).divide(DIVISOR);
    }

    @Override
    public void apply(Product product, List<Product> products, Map<Product, BigDecimal> discountByProduct) {
        int noOfProducts = products.stream()
                .filter(p -> p.getId() == product.getId())
                .collect(Collectors.toList())
                .size();
        if(noOfProducts > 1) {
            BigDecimal calculatedDiscount = (product.getPrice().multiply(discount));
            discountByProduct.put(product,
                    discountByProduct.getOrDefault(discountByProduct, ZERO)
                            .add(calculatedDiscount).setScale(2));
        }
    }
}
