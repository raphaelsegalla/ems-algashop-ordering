package com.algaworks.algashop.ordering.domain.entity;

import com.algaworks.algashop.ordering.domain.exception.OrderCannotBeEditedException;
import com.algaworks.algashop.ordering.domain.exception.OrderInvalidShippingDeliveryDateException;
import com.algaworks.algashop.ordering.domain.exception.OrderStatusCannotBeChangedException;
import com.algaworks.algashop.ordering.domain.exception.ProductOutOfStockException;
import com.algaworks.algashop.ordering.domain.valueobject.Billing;
import com.algaworks.algashop.ordering.domain.valueobject.Money;
import com.algaworks.algashop.ordering.domain.valueobject.Product;
import com.algaworks.algashop.ordering.domain.valueobject.ProductName;
import com.algaworks.algashop.ordering.domain.valueobject.Quantity;
import com.algaworks.algashop.ordering.domain.valueobject.Shipping;
import com.algaworks.algashop.ordering.domain.valueobject.id.CustomerId;
import com.algaworks.algashop.ordering.domain.valueobject.id.ProductId;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertWith;

class OrderTest {

    @Test
    void shouldGenerateDraftOrder() {
        CustomerId customerId = new CustomerId();
        Order order = Order.draft(customerId);

        Assertions.assertWith(order,
                o -> assertThat(o.id()).isNotNull(),
                o -> assertThat(o.customerId()).isEqualTo(customerId),
                o -> assertThat(o.totalAmount()).isEqualTo(Money.ZERO),
                o -> assertThat(o.totalItems()).isEqualTo(Quantity.ZERO),
                o -> assertThat(o.isDraft()).isTrue(),
                o -> assertThat(o.items()).isEmpty(),

                o -> assertThat(o.placedAt()).isNull(),
                o -> assertThat(o.paidAt()).isNull(),
                o -> assertThat(o.canceledAt()).isNull(),
                o -> assertThat(o.readyAt()).isNull(),
                o -> assertThat(o.billing()).isNull(),
                o -> assertThat(o.shipping()).isNull(),
                o -> assertThat(o.paymentMethod()).isNull()
        );
    }

    @Test
    void shouldAddItem() {
        Order order = Order.draft(new CustomerId());
        Product product = ProductTestDataBuilder.aProductAltMousePad().build();
        ProductId productId = product.id();

        order.addItem(product, new Quantity(1));

        assertThat(order.items().size()).isEqualTo(1);

        OrderItem orderItem = order.items().iterator().next();

        assertWith(orderItem,
                i -> assertThat(i.id()).isNotNull(),
                i -> assertThat(i.productName()).isEqualTo(new ProductName("Mouse Pad")),
                i -> assertThat(i.productId()).isEqualTo(productId),
                i -> assertThat(i.price()).isEqualTo(new Money("100")),
                i -> assertThat(i.quantity()).isEqualTo(new Quantity(1))
        );
    }

    @Test
    void shouldGenerateExceptionWhenTryToChangeItemSet() {
        Order order = Order.draft(new CustomerId());
        Product product = ProductTestDataBuilder.aProductAltMousePad().build();

        order.addItem(
                product,
                new Quantity(1)
        );

        Set<OrderItem> items = order.items();

        assertThatExceptionOfType(UnsupportedOperationException.class)
                .isThrownBy(items::clear);
    }

    @Test
    void shouldCalculateTotals() {
        Order order = Order.draft(new CustomerId());

        order.addItem(
                ProductTestDataBuilder.aProductAltMousePad().build(),
                new Quantity(2)
        );

        order.addItem(
                ProductTestDataBuilder.aProductAltRamMemory().build(),
                new Quantity(1)
        );

        assertThat(order.totalAmount()).isEqualTo(new Money("400"));
        assertThat(order.totalItems()).isEqualTo(new Quantity(3));
    }

    @Test
    void givenDraftOrder_whenPlace_shouldChangeToPlaced() {
        Order order = OrderTestDataBuilder.anOrder().status(OrderStatus.PLACED).build();
        assertThat(order.isPlaced()).isTrue();
    }

    @Test
    void givenPlacedOrder_whenMarkAsPaid_shouldChangeToPaid() {
        Order order = OrderTestDataBuilder.anOrder().status(OrderStatus.PLACED).build();
        order.markAsPaid();
        assertThat(order.isPaid()).isTrue();
        assertThat(order.paidAt()).isNotNull();
    }

    @Test
    void givenPlacedOrder_whenTryToPlace_shouldGenerateException() {
        Order order = OrderTestDataBuilder.anOrder().status(OrderStatus.PLACED).build();
        assertThatExceptionOfType(OrderStatusCannotBeChangedException.class)
                .isThrownBy(order::place);
    }

    @Test
    void givenDraftOrder_whenChangePaymentMethod_shouldAllowChange() {
        Order order = Order.draft(new CustomerId());
        order.changePaymentMethod(PaymentMethod.CREDIT_CARD);
        assertWith(order.paymentMethod()).isEqualTo(PaymentMethod.CREDIT_CARD);
    }

    @Test
    void givenDraftOrder_whenChangeBilling_shouldAllowChange() {
        Billing billing = OrderTestDataBuilder.aBilling();

        Order order = Order.draft(new CustomerId());
        order.changeBilling(billing);

        assertThat(order.billing()).isEqualTo(billing);
    }

    @Test
    void givenDraftOrder_whenChangeShipping_shouldAllowChange() {

        Shipping shipping = OrderTestDataBuilder.aShipping();

        Order order = Order.draft(new CustomerId());

        order.changeShipping(shipping);

        assertWith(order, o -> assertThat(o.shipping()).isEqualTo(shipping));
    }

    @Test
    void givenDraftOrderAndDeliveryDateInjThePast_whenChangeShipping_shouldNotAllowChange() {
        LocalDate expectedDeliveryDate = LocalDate.now().minusDays(1);

        Shipping shipping = OrderTestDataBuilder.aShipping().toBuilder()
                .expectedDate(expectedDeliveryDate)
                .build();

        Order order = Order.draft(new CustomerId());

        assertThatExceptionOfType(OrderInvalidShippingDeliveryDateException.class)
                .isThrownBy(() -> order.changeShipping(shipping));
    }

    @Test
    void givenDraftOrder_whenChangeItem_shouldRecalculate() {
        Order order = Order.draft(new CustomerId());

        order.addItem(
                ProductTestDataBuilder.aProductAltMousePad().build(),
                new Quantity(5)
        );

        OrderItem orderItem = order.items().iterator().next();

        order.changeItemQuantity(orderItem.id(), new Quantity(5));

        Assertions.assertWith(order,
                o -> Assertions.assertThat(o.totalAmount()).isEqualTo(new Money("500.00")),
                o -> Assertions.assertThat(o.totalItems()).isEqualTo(new Quantity(5))
        );
    }

    @Test
    void givenOutOfStockProduct_whenTryToAddAnOrder_shouldNotAllow() {
        Order order = Order.draft(new CustomerId());

        ThrowableAssert.ThrowingCallable addItemTask = () -> order.addItem(ProductTestDataBuilder.aProductUnavailable().build(), new Quantity(1));

        assertThatExceptionOfType(ProductOutOfStockException.class)
                .isThrownBy(addItemTask);
    }

    @Test
    void givenDraftOrder_whenChangeItemShippingBillingPayment_shouldNotError() {
        Order order = Order.draft(new CustomerId());
        Billing billing = OrderTestDataBuilder.aBilling();
        Shipping shipping = OrderTestDataBuilder.aShipping();
        order.addItem(
                ProductTestDataBuilder.aProductAltMousePad().build(),
                new Quantity(5)
        );
        order.changePaymentMethod(PaymentMethod.CREDIT_CARD);
        order.changeBilling(billing);
        order.changeShipping(shipping);

        Assertions.assertWith(order,
                o -> Assertions.assertThat(o.totalAmount()).isEqualTo(new Money("500.00")),
                o -> Assertions.assertThat(o.totalItems()).isEqualTo(new Quantity(5)),
                o -> Assertions.assertThat(o.paymentMethod()).isEqualTo(PaymentMethod.CREDIT_CARD),
                o -> Assertions.assertThat(o).isNotNull(),
                o -> Assertions.assertThat(o.shipping()).isNotNull(),
                o -> Assertions.assertThat(o.billing()).isNotNull()
        );
    }

    @Test
    void givenPlacedOrder_whenTryToAddItem_shouldNotAllow() {
        Order order = OrderTestDataBuilder.anOrder().status(OrderStatus.PLACED).build();

        ThrowableAssert.ThrowingCallable addItemTask = () -> order.addItem(ProductTestDataBuilder.aProductUnavailable().build(), new Quantity(1));

        assertThatExceptionOfType(OrderCannotBeEditedException.class)
                .isThrownBy(addItemTask);
    }

    @Test
    void givenPlacedOrder_whenTryToChangePaymentMethod_shouldNotAllow() {
        Order order = OrderTestDataBuilder.anOrder().status(OrderStatus.PLACED).build();

        ThrowableAssert.ThrowingCallable addItemTask = () -> order.changePaymentMethod(PaymentMethod.CREDIT_CARD);

        assertThatExceptionOfType(OrderCannotBeEditedException.class)
                .isThrownBy(addItemTask);
    }

    @Test
    void givenPlacedOrder_whenTryToChangeShipping_shouldNotAllow() {
        Order order = OrderTestDataBuilder.anOrder().status(OrderStatus.PLACED).build();
        Shipping shipping = OrderTestDataBuilder.aShipping();

        ThrowableAssert.ThrowingCallable addItemTask = () -> order.changeShipping(shipping);

        assertThatExceptionOfType(OrderCannotBeEditedException.class)
                .isThrownBy(addItemTask);
    }

    @Test
    void givenPlacedOrder_whenTryToChangeBilling_shouldNotAllow() {
        Order order = OrderTestDataBuilder.anOrder().status(OrderStatus.PLACED).build();
        Billing billing = OrderTestDataBuilder.aBilling();

        ThrowableAssert.ThrowingCallable addItemTask = () -> order.changeBilling(billing);

        assertThatExceptionOfType(OrderCannotBeEditedException.class)
                .isThrownBy(addItemTask);
    }

}