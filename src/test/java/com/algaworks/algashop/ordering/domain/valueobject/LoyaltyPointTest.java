package com.algaworks.algashop.ordering.domain.valueobject;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class LoyaltyPointTest {

    @Test
    void shouldGenerate() {
        LoyaltyPoint loyaltyPoint = new LoyaltyPoint(10);
        Assertions.assertThat(loyaltyPoint.value()).isEqualTo(10);
    }

    @Test
    void shouldAddValue() {
        LoyaltyPoint loyaltyPoints = new LoyaltyPoint(10);
        Assertions.assertThat(loyaltyPoints.add(5).value()).isEqualTo(15);
    }

    @Test
    void shouldNotAddValue() {
        LoyaltyPoint loyaltyPoint = new LoyaltyPoint(10);
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                        .isThrownBy(() -> loyaltyPoint.add(-5));

        Assertions.assertThat(loyaltyPoint.value()).isEqualTo(10);
    }

    @Test
    void shouldNotAddZeroValue() {
        LoyaltyPoint loyaltyPoint = new LoyaltyPoint(10);
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> loyaltyPoint.add(0));

        Assertions.assertThat(loyaltyPoint.value()).isEqualTo(10);
    }

}