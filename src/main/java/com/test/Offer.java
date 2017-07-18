package com.test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface Offer {

    void apply(Product product, List<Product> itemsInCart, Map<Product, BigDecimal> discountByProduct);
}
