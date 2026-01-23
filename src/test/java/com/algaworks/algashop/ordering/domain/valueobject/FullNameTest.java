package com.algaworks.algashop.ordering.domain.valueobject;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class FullNameTest {

    @Test
    void shouldGenerate() {
        FullName fullName = new FullName("John", "Doe");
        Assertions.assertThat(fullName).isEqualTo(new FullName("John", "Doe"));
    }

    @Test
    void shouldNotGenerateFirstnameNullValue() {
        Assertions.assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> new FullName(null, "Doe"));
    }

    @Test
    void shouldNotGenerateLastnameNullValue() {
        Assertions.assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> new FullName("John", null));
    }

    @Test
    void shouldNotGenerateFirstnameBlankValue() {
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> new FullName("      ", "Doe"));
    }

    @Test
    void shouldNotGenerateLastnameBlankValue() {
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> new FullName("John", "      "));
    }

    @Test
    void shouldGenerateAndReturnToString() {
        FullName fullName = new FullName("John", "Doe");
        Assertions.assertThat(fullName.toString()).hasToString("John Doe");
    }

}