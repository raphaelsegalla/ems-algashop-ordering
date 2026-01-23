package com.algaworks.algashop.ordering.domain.valueobject;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.UUID;

class CustomerIdTest {

    @Test
    void shouldGenerate() {
        CustomerId customerId = new CustomerId();
        Assertions.assertThat(customerId.value()).isInstanceOf(UUID.class);
    }

    @Test
    void shouldNotGenerateNullValue() {
        Assertions.assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> new CustomerId(null));
    }

    @Test
    void shouldNotGenerateInvalidValue() {
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> new CustomerId(UUID.fromString("b82ad8aa-ea27-4325-b201-7a5a648ef0e777777777")));
    }

    @Test
    void shouldGenerateAndReturnToString() {
        CustomerId customerId = new CustomerId(UUID.fromString("b82ad8aa-ea27-4325-b201-7a5a648ef0e7"));
        Assertions.assertThat(customerId.toString()).hasToString("b82ad8aa-ea27-4325-b201-7a5a648ef0e7");
    }

}