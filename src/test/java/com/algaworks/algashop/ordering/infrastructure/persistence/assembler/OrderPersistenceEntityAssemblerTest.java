package com.algaworks.algashop.ordering.infrastructure.persistence.assembler;

import com.algaworks.algashop.ordering.domain.model.entity.Order;
import com.algaworks.algashop.ordering.domain.model.entity.OrderTestDataBuilder;
import com.algaworks.algashop.ordering.infrastructure.persistence.entity.OrderPersistenceEntity;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class OrderPersistenceEntityAssemblerTest {

    private final OrderPersistenceEntityAssembler assembler = new OrderPersistenceEntityAssembler();

    @Test
    void shouldConvertToDomain() {
        Order order = OrderTestDataBuilder.anOrder().build();
        OrderPersistenceEntity orderPersistenceEntity = assembler.fromDomain(order);
        assertThat(orderPersistenceEntity).satisfies(
                entity -> assertThat(entity.getId()).isEqualTo(order.id().value().toLong()),
                entity -> assertThat(entity.getCustomerId()).isEqualTo(order.customerId().value()),
                entity -> assertThat(entity.getTotalAmount()).isEqualTo(order.totalAmount().value()),
                entity -> assertThat(entity.getTotalItems()).isEqualTo(order.totalItems().value()),
                entity -> assertThat(entity.getStatus()).isEqualTo(order.status().name()),
                entity -> assertThat(entity.getPaymentMethod()).isEqualTo(order.paymentMethod().name()),
                entity -> assertThat(entity.getPlacedAt()).isEqualTo(order.placedAt()),
                entity -> assertThat(entity.getPaidAt()).isEqualTo(order.paidAt()),
                entity -> assertThat(entity.getCanceledAt()).isEqualTo(order.canceledAt()),
                entity -> assertThat(entity.getReadyAt()).isEqualTo(order.readyAt())
        );
    }

    @Test
    void shouldMerge() {
        // Implement test for merging entities
    }

}