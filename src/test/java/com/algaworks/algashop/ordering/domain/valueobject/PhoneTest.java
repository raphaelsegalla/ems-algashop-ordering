package com.algaworks.algashop.ordering.domain.valueobject;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class PhoneTest {

    @Test
    void shouldGenerate() {
        Phone phone = new Phone("478-256-2504");
        Assertions.assertThat(phone).isEqualTo(new Phone("478-256-2504"));
    }

    @Test
    void shouldNotGenerateNullValue() {
        Assertions.assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> new Phone(null));
    }

    @Test
    void shouldNotGenerateBlankValue() {
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> new Phone("      "));
    }

    @Test
    void shouldGenerateAndReturnToString() {
        Phone phone = new Phone("478-256-2504");
        Assertions.assertThat(phone.toString()).hasToString("478-256-2504");
    }

}