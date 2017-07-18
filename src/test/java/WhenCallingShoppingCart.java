import com.test.*;
import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.math.BigDecimal;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class WhenCallingShoppingCart {

    ShoppingCart shoppingCart;

    Product soap = Product.builder()
            .id(1l)
            .name("Dove Soap")
            .price(new BigDecimal("30"))
            .offer(new BuyXGetYOffer(2, 1))
            .build();
    Product deo = Product.builder()
            .id(2l)
            .name("Axe Deo")
            .price(new BigDecimal("100"))
            .build();

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void setup() {
        shoppingCart = new ShoppingCart();
    }

    @Test
    public void should_add_product_in_the_cart() {
        // when
        shoppingCart.add(soap, 5);

        // then
        assertThat(shoppingCart.getProducts().size(), is(5));
        assertThat(shoppingCart.getTotalPrice(), CoreMatchers.is(new BigDecimal("120.00")));
    }

    @Test
    public void should_throw_exception() {
        expectedException.expect(Exception.class);
        Product.builder().name("123").build();
    }

    @Test
    public void should_add_products_with_offer_and_return_total_discount_for_3_soaps() {
        // when
        shoppingCart.add(soap, 3);

        // then
        assertThat(shoppingCart.getProducts().size(), is(3));
        assertThat(shoppingCart.getTotalPrice(), is(new BigDecimal("60.00")));
        assertThat(shoppingCart.getDiscount(), is(new BigDecimal("30.00")));
    }

    @Test
    public void should_add_products_with_offer_and_return_total_discount_for_5_soaps() {
        // when
        shoppingCart.add(soap, 5);

        // then
        assertThat(shoppingCart.getProducts().size(), is(5));
        assertThat(shoppingCart.getTotalPrice(), is(new BigDecimal("120.00")));
        assertThat(shoppingCart.getDiscount(), is(new BigDecimal("30.00")));
    }

    @Test
    public void should_add_products_with_offer_and_return_total_discount_for_3_soaps_and_2_deos() {
        // when
        shoppingCart.add(soap, 3);
        shoppingCart.add(deo, 2);

        // then
        assertThat(shoppingCart.getProducts().size(), is(5));
        assertThat(shoppingCart.getTotalPrice(), is(new BigDecimal("260.00")));
        assertThat(shoppingCart.getDiscount(), is(new BigDecimal("30.00")));
    }

    @Test
    public void should_apply_buy_1_get_50_percent_discount_offer_on_eligible_products() {
        // given
        Product doveSoap = Product.builder()
                .id(1l)
                .name("Dove")
                .price(new BigDecimal("30.0"))
                .offer(new Buy1GetDiscountOnNextOne(50))
                .build();

        // when
        shoppingCart.add(doveSoap, 2);

        // then
        assertThat(shoppingCart.getProducts().size(), is(2));
        assertThat(shoppingCart.getTotalPrice(), is(new BigDecimal("45.00")));
        assertThat(shoppingCart.getDiscount(), is(new BigDecimal("15.00")));
    }

    @Test
    public void should_apply_discount_on_total_price() {
        // given
        Product dove = Product.builder()
                .id(1l)
                .name("Dove")
                .price(new BigDecimal("30.00"))
                .build();
        Product axe = Product.builder()
                .id(2l)
                .name("Axe")
                .price(new BigDecimal("100.00"))
                .build();
        Offer totalPriceOffer = new TotalPriceOffer(20, new BigDecimal(500));
        // when
        shoppingCart.setOffer(totalPriceOffer);
        shoppingCart.add(dove, 5);
        shoppingCart.add(axe, 4);

        // then
        assertThat(shoppingCart.getProducts().size(), is(9));
        assertThat(shoppingCart.getTotalPrice(dove), is(new BigDecimal("120.00")));
        assertThat(shoppingCart.getTotalPrice(axe), is(new BigDecimal("320.00")));
        assertThat(shoppingCart.getTotalPrice(), is(new BigDecimal("440.00")));
        assertThat(shoppingCart.getDiscountByProduct().get(dove), is(new BigDecimal("30.00")));
        assertThat(shoppingCart.getTotalPrice(axe), is(new BigDecimal("320.00")));
        assertThat(shoppingCart.getTotalPrice(), is(new BigDecimal("440.00")));
    }
}
