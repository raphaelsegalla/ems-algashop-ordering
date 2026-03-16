package com.algaworks.algashop.ordering.domain.valueobject;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

class QuantityTest {

    @Test
    void shouldGenerate() {
        Quantity quantity = new Quantity(10);
        Assertions.assertThat(quantity).isEqualTo(new Quantity(10));
    }

    @Test
    void shouldNotGenerateQuantityNullValue() {
        Assertions.assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> new Quantity(null));
    }

    @Test
    void shouldNotGenerateQuantityNegativeValue() {
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> new Quantity(-10));
    }

    @Test
    void shouldGenerateAndReturnToString() {
        Quantity quantity = new Quantity(10);
        Assertions.assertThat(quantity.toString()).isEqualTo("10");
    }

    @Test
    void shouldCompareValues() {
        Quantity quantity = new Quantity(10);
        Quantity quantityToCompare = new Quantity(12);
        int compare = quantity.compareTo(quantityToCompare);
        Assertions.assertThat(compare).isEqualTo(-1);
    }

}