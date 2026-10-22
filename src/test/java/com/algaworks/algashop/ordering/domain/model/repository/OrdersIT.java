package com.algaworks.algashop.ordering.domain.model.repository;

import com.algaworks.algashop.ordering.domain.model.entity.CustomerTestDataBuilder;
import com.algaworks.algashop.ordering.domain.model.entity.Order;
import com.algaworks.algashop.ordering.domain.model.entity.OrderStatus;
import com.algaworks.algashop.ordering.domain.model.entity.OrderTestDataBuilder;
import com.algaworks.algashop.ordering.domain.model.valueobject.Money;
import com.algaworks.algashop.ordering.domain.model.valueobject.id.CustomerId;
import com.algaworks.algashop.ordering.domain.model.valueobject.id.OrderId;
import com.algaworks.algashop.ordering.infrastructure.persistence.assembler.CustomerPersistenceEntityAssembler;
import com.algaworks.algashop.ordering.infrastructure.persistence.assembler.OrderPersistenceEntityAssembler;
import com.algaworks.algashop.ordering.infrastructure.persistence.disassembler.CustomerPersistenceEntityDisassembler;
import com.algaworks.algashop.ordering.infrastructure.persistence.disassembler.OrderPersistenceEntityDisassembler;
import com.algaworks.algashop.ordering.infrastructure.persistence.provider.CustomersPersistenceProvider;
import com.algaworks.algashop.ordering.infrastructure.persistence.provider.OrdersPersistenceProvider;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.Year;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

@DataJpaTest
@Import({
        OrdersPersistenceProvider.class,
        OrderPersistenceEntityAssembler.class,
        OrderPersistenceEntityDisassembler.class,
        CustomersPersistenceProvider.class,
        CustomerPersistenceEntityAssembler.class,
        CustomerPersistenceEntityDisassembler.class
})
class OrdersIT {

    private final Orders orders;
    private final Customers customers;
    private final TransactionTemplate newTransaction;

    @Autowired
    public OrdersIT(Orders orders, Customers customers, PlatformTransactionManager transactionManager) {
        this.orders = orders;
        this.customers = customers;
        this.newTransaction = new TransactionTemplate(transactionManager);
        this.newTransaction.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
    }

    @BeforeEach
    public void setup() {
        if(!customers.exists(CustomerTestDataBuilder.DEFAULT_CUSTOMER_ID)) {
            customers.add(
                    CustomerTestDataBuilder.existingCustomer().build()
            );
        }
    }

    @Test
    void shouldPersistAndFind() {
        Order originalOrder = OrderTestDataBuilder.anOrder().build();
        OrderId orderId = originalOrder.id();
        orders.add(originalOrder);

        Optional<Order> possibleOrder = orders.ofId(orderId);

        Assertions.assertThat(possibleOrder).isPresent();

        Order savedOrder = possibleOrder.get();

        Assertions.assertThat(savedOrder).satisfies(
                s -> Assertions.assertThat(s.id()).isEqualTo(orderId),
                s -> Assertions.assertThat(s.customerId()).isEqualTo(originalOrder.customerId()),
                s -> Assertions.assertThat(s.totalAmount()).isEqualTo(originalOrder.totalAmount()),
                s -> Assertions.assertThat(s.totalItems()).isEqualTo(originalOrder.totalItems()),
                s -> Assertions.assertThat(s.placedAt()).isEqualTo(originalOrder.placedAt()),
                s -> Assertions.assertThat(s.paidAt()).isEqualTo(originalOrder.paidAt()),
                s -> Assertions.assertThat(s.canceledAt()).isEqualTo(originalOrder.canceledAt()),
                s -> Assertions.assertThat(s.readyAt()).isEqualTo(originalOrder.readyAt()),
                s -> Assertions.assertThat(s.status()).isEqualTo(originalOrder.status()),
                s -> Assertions.assertThat(s.paymentMethod()).isEqualTo(originalOrder.paymentMethod())
        );

    }

    @Test
    void shouldUpdateExistingOrder() {
        Order order = OrderTestDataBuilder.anOrder().status(OrderStatus.PLACED).build();
        orders.add(order);

        order = orders.ofId(order.id()).orElseThrow();
        order.markAsPaid();

        orders.add(order);

        order = orders.ofId(order.id()).orElseThrow();

        Assertions.assertThat(order.isPaid()).isTrue();
    }

    @Test
    void shouldNotAllowStaleUpdates() {
        Order order = OrderTestDataBuilder.anOrder().status(OrderStatus.PLACED).build();
        orders.add(order);

        Order order1 = orders.ofId(order.id()).orElseThrow();
        Order order2 = orders.ofId(order.id()).orElseThrow();

        order1.markAsPaid();
        orders.add(order1);

        order2.cancel();

        Assertions.assertThatExceptionOfType(ObjectOptimisticLockingFailureException.class)
                .isThrownBy(() -> orders.add(order2));

        Order savedOrder = orders.ofId(order.id()).orElseThrow();

        Assertions.assertThat(savedOrder.canceledAt()).isNull();
        Assertions.assertThat(savedOrder.paidAt()).isNotNull();
    }

    @Test
    public void shouldCountExistingOrders() {
        Assertions.assertThat(orders.count()).isZero();

        Order order1 = OrderTestDataBuilder.anOrder().build();
        Order order2 = OrderTestDataBuilder.anOrder().build();

        orders.add(order1);
        orders.add(order2);

        Assertions.assertThat(orders.count()).isEqualTo(2L);
    }

    @Test
    public void shouldReturnIfOrderExists() {
        Order order = OrderTestDataBuilder.anOrder().build();
        orders.add(order);

        Assertions.assertThat(orders.exists(order.id())).isTrue();
        Assertions.assertThat(orders.exists(new OrderId())).isFalse();

    }

    @Test
    void shouldListExistingOrdersByCustomerInYear() {
        Order order1 = OrderTestDataBuilder.anOrder().status(OrderStatus.PLACED).build();
        Order order2 = OrderTestDataBuilder.anOrder().status(OrderStatus.PLACED).build();
        Order order3 = OrderTestDataBuilder.anOrder().status(OrderStatus.CANCELED).build();
        Order order4 = OrderTestDataBuilder.anOrder().status(OrderStatus.DRAFT).build();

        orders.add(order1);
        orders.add(order2);
        orders.add(order3);
        orders.add(order4);

        CustomerId customerId = CustomerTestDataBuilder.DEFAULT_CUSTOMER_ID;

        List<Order> listedOrders = orders.placedByCustomerInYear(customerId, Year.now());

        Assertions.assertThat(listedOrders).hasSize(2);
        Assertions.assertThat(listedOrders).isNotEmpty();

        listedOrders = orders.placedByCustomerInYear(customerId, Year.now().minusYears(1));
        Assertions.assertThat(listedOrders).isEmpty();

        listedOrders = orders.placedByCustomerInYear(new CustomerId(), Year.now());
        Assertions.assertThat(listedOrders).isEmpty();
    }

    @Test
    public void shouldReturnTotalSoldByCustomer() {
        Order order1 = OrderTestDataBuilder.anOrder().status(OrderStatus.PAID).build();
        Order order2 = OrderTestDataBuilder.anOrder().status(OrderStatus.PAID).build();

        orders.add(order1);
        orders.add(order2);

        orders.add(
                OrderTestDataBuilder.anOrder().status(OrderStatus.CANCELED).build()
        );

        orders.add(
                OrderTestDataBuilder.anOrder().status(OrderStatus.PLACED).build()
        );

        Money expectedTotalAmount = order1.totalAmount().add(order2.totalAmount());

        CustomerId customerId = CustomerTestDataBuilder.DEFAULT_CUSTOMER_ID;

        Assertions.assertThat(orders.totalSoldForCustomer(customerId)).isEqualTo(expectedTotalAmount);

        Assertions.assertThat(orders.totalSoldForCustomer(new CustomerId())).isEqualTo(Money.ZERO);

    }

    @Test
    public void shouldReturnSalesQuantityByCustomer() {
        Order order1 = OrderTestDataBuilder.anOrder().status(OrderStatus.PAID).build();
        Order order2 = OrderTestDataBuilder.anOrder().status(OrderStatus.PAID).build();

        orders.add(order1);
        orders.add(order2);

        orders.add(
                OrderTestDataBuilder.anOrder().status(OrderStatus.CANCELED).build()
        );

        orders.add(
                OrderTestDataBuilder.anOrder().status(OrderStatus.PLACED).build()
        );

        CustomerId customerId = CustomerTestDataBuilder.DEFAULT_CUSTOMER_ID;

        Assertions.assertThat(orders.salesQuantityByCustomerInYear(customerId, Year.now())).isEqualTo(2L);
        Assertions.assertThat(orders.salesQuantityByCustomerInYear(customerId, Year.now().minusYears(1))).isZero();
    }

    @Test
    void shouldNotAllowStaleUpdatesReal() {
        // T0: insere o pedido em transação própria
        OrderId orderId = inNewTransaction(() -> {
            Order order = OrderTestDataBuilder.anOrder().status(OrderStatus.PLACED).build();
            orders.add(order);
            return order.id();
        });

        Assertions.assertThatExceptionOfType(ObjectOptimisticLockingFailureException.class)
                .isThrownBy(() -> inNewTransaction(() -> {
                    // T1: carrega o pedido em sua própria transação
                    Order orderT1 = orders.ofId(orderId).orElseThrow();

                    // T2: em outra transação separada, salva primeiro
                    inNewTransaction(() -> {
                        Order orderT2 = orders.ofId(orderId).orElseThrow();
                        orderT2.markAsPaid();
                        orders.add(orderT2);
                    });

                    // T1 tenta salvar com versão obsoleta
                    orderT1.cancel();
                    orders.add(orderT1);
                }));

        // Verifica que a atualização de T2 prevaleceu
        Order savedOrder = orders.ofId(orderId).orElseThrow();
        Assertions.assertThat(savedOrder.canceledAt()).isNull();
        Assertions.assertThat(savedOrder.paidAt()).isNotNull();
    }

    private <T> T inNewTransaction(Supplier<T> callback) {
        return newTransaction.execute(status -> callback.get());
    }

    private void inNewTransaction(Runnable callback) {
        newTransaction.executeWithoutResult(status -> callback.run());
    }
}