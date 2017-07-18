package com.test;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Data
public class TotalPriceOffer implements Offer {

    public static final BigDecimal DIVISOR = new BigDecimal("100");
    public static final BigDecimal ZERO = new BigDecimal("0.0");
    private BigDecimal discount;
    private BigDecimal totalPrice;

    public TotalPriceOffer(int discountPercent, BigDecimal totalPrice) {
        discount = new BigDecimal(String.valueOf(discountPercent)).divide(DIVISOR);
        this.totalPrice = totalPrice;
    }

    @Override
    public void apply(Product product, List<Product> products, Map<Product, BigDecimal> discountByProduct) {
        if(getTotalPriceBeforeDiscount(products).compareTo(totalPrice) > 0) {
            products.stream().forEach(p -> {
                discountByProduct.put(p, discountByProduct.getOrDefault(p, ZERO)
                        .add(p.getPrice().multiply(discount)));
            });
        }
    }

    private BigDecimal getTotalPriceBeforeDiscount(List<Product> products) {
        return products.stream()
                .map(Product::getPrice)
                .reduce((a,b) -> a.add(b).setScale(2)).get();
    }
}
