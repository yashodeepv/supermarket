import com.test.*;
import org.hamcrest.CoreMatchers;
import org.hamcrest.Matcher;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.stream.Stream;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ShoppingCartTest {

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
    public void shouldAddProductInTheCart() {
        // when
        shoppingCart.add(soap, 5);

        // then
        assertThat(shoppingCart.getProducts().size(), is(5));
        assertThat(shoppingCart.getTotalPrice(), CoreMatchers.is(new BigDecimal("120.00")));
    }

    @Test
    public void shouldThrowPriceNotFoundException() {
        expectedException.expect(Exception.class);
        Product p = Product.builder().name("123").build();
    }

    @Test
    public void shouldAddProductsWithOfferAndReturnTotalDiscountFor3Soaps() {
        // given


        // when
        shoppingCart.add(soap, 3);

        // then
        assertThat(shoppingCart.getProducts().size(), is(3));
        assertThat(shoppingCart.getTotalPrice(), is(new BigDecimal("60.00")));
        assertThat(shoppingCart.getDiscount(), is(new BigDecimal("30.00")));
    }

    @Test
    public void shouldAddProductsWithOfferAndReturnTotalDiscountFor5Soaps() {
        // given


        // when
        shoppingCart.add(soap, 5);

        // then
        assertThat(shoppingCart.getProducts().size(), is(5));
        assertThat(shoppingCart.getTotalPrice(), is(new BigDecimal("120.00")));
        assertThat(shoppingCart.getDiscount(), is(new BigDecimal("30.00")));
    }

    @Test
    public void shouldAddProductsWithOfferAndReturnTotalDiscountFor3SoapsAnd2Deos() {
        // given


        // when
        shoppingCart.add(soap, 3);
        shoppingCart.add(deo, 2);

        // then
        assertThat(shoppingCart.getProducts().size(), is(5));
        assertThat(shoppingCart.getTotalPrice(), is(new BigDecimal("260.00")));
        assertThat(shoppingCart.getDiscount(), is(new BigDecimal("30.00")));
    }

    @Test
    public void shouldApplyBuy1Get50PercentDiscountOfferOnEligibleProducts() {
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
    public void shouldApplyDiscountOnTotalPrice() {
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
        assertThat(shoppingCart.getDiscountByProduct().get(soap), is(new BigDecimal("30.00")));
        assertThat(shoppingCart.getTotalPrice(axe), is(new BigDecimal("320.00")));
        assertThat(shoppingCart.getTotalPrice(), is(new BigDecimal("440.00")));




    }

}
