package com.algaworks.algashop.ordering.infrastructure.persistence.assembler;

import com.algaworks.algashop.ordering.domain.model.entity.Order;
import com.algaworks.algashop.ordering.domain.model.entity.OrderItem;
import com.algaworks.algashop.ordering.domain.model.entity.OrderTestDataBuilder;
import com.algaworks.algashop.ordering.infrastructure.persistence.entity.OrderItemPersistenceEntity;
import com.algaworks.algashop.ordering.infrastructure.persistence.entity.OrderPersistenceEntity;
import com.algaworks.algashop.ordering.infrastructure.persistence.entity.OrderPersistenceEntityTestDataBuilder;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

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
    void givenOrderWithNotItems_shouldRemovedPersistenceEntityItems() {
        Order order = OrderTestDataBuilder.anOrder().withItems(false).build();
        OrderPersistenceEntity orderPersistenceEntity = OrderPersistenceEntityTestDataBuilder.existingOrder().build();

        Assertions.assertThat(order.items()).isEmpty();
        Assertions.assertThat(orderPersistenceEntity.getItems()).isNotEmpty();

        assembler.merge(orderPersistenceEntity, order);

        Assertions.assertThat(orderPersistenceEntity.getItems()).isEmpty();
    }

    @Test
    void givenOrderWithItems_shouldAddToPersistenceEntity() {
        Order order = OrderTestDataBuilder.anOrder().withItems(true).build();
        OrderPersistenceEntity orderPersistenceEntity = OrderPersistenceEntityTestDataBuilder.existingOrder().items(new HashSet<>()).build();

        Assertions.assertThat(order.items()).isNotEmpty();
        Assertions.assertThat(orderPersistenceEntity.getItems()).isEmpty();

        assembler.merge(orderPersistenceEntity, order);

        Assertions.assertThat(orderPersistenceEntity.getItems()).isNotEmpty();
        Assertions.assertThat(orderPersistenceEntity.getItems().size()).isEqualTo(order.items().size());
    }

    @Test
    void givenOrderWithItems_whenMerge_shouldRemoveMergeCorrectly() {
        Order order = OrderTestDataBuilder.anOrder().withItems(true).build();

        Assertions.assertThat(order.items().size()).isEqualTo(2);

        Set<OrderItemPersistenceEntity> orderItemPersistenceEntities = order.items().stream().map(assembler::fromDomain).collect(Collectors.toSet());

        OrderPersistenceEntity persistenceEntity = OrderPersistenceEntityTestDataBuilder.existingOrder().items(orderItemPersistenceEntities).build();

        OrderItem orderItem = order.items().iterator().next();
        order.removeItem(orderItem.id());

        assembler.merge(persistenceEntity, order);

        Assertions.assertThat(persistenceEntity.getItems()).isNotEmpty();
        Assertions.assertThat(persistenceEntity.getItems().size()).isEqualTo(order.items().size());
    }
}