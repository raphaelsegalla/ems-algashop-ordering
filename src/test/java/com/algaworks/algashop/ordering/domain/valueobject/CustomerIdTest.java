package com.algaworks.algashop.ordering.domain.valueobject;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.UUID;

class CustomerIdTest {

    @Test
    void shouldGenerate() {
        CustomerId customerId = new CustomerId();
        Assertions.assertThat(customerId.value()).isInstanceOf(UUID.class);
    }

}