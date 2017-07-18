package com.test;


import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

@Data
public class ShoppingCart {

    private List<Product> products = new ArrayList<>();
    private Offer offer;
    private Map<Product, BigDecimal> discountByProduct = new HashMap<>();

    private void applyOffer(Product product) {
        if(product.getOffer() != null) {
            product.getOffer().apply(product, products, discountByProduct);
        }
        if(offer != null) {
            offer.apply(product, products, discountByProduct);
        }
    }

    private BigDecimal getTotalPriceBeforeDiscount() {
        return products.stream()
                .map(Product::getPrice)
                .reduce((a,b) -> a.add(b).setScale(2)).get();
    }

    public BigDecimal getTotalPrice() {
        return getTotalPriceBeforeDiscount()
                .subtract(getDiscount());
    }

    public BigDecimal getTotalPrice(Product prod) {
        return products.stream()
                .filter(p -> p.equals(prod))
                .map(Product::getPrice)
                .reduce((a,b) -> a.add(b)).get()
                .subtract(discountByProduct.get(prod))
                .setScale(2);
    }

    public void add(Product product, int i) {
        IntStream.range(0, i).forEach(a -> products.add(product));
        applyOffer(product);
    }

    public void setOffer(Offer offer) {
        this.offer = offer;
    }

    public BigDecimal getDiscount() {
        return discountByProduct.values().stream()
                .reduce((a, b) -> a.add(b).setScale(2))
                .get()
                .setScale(2);
    }
}
