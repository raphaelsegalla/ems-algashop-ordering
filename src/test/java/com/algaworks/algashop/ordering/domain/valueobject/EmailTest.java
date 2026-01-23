package com.algaworks.algashop.ordering.domain.valueobject;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class EmailTest {

    @Test
    void shouldGenerate() {
        Email email = new Email("john.doe@gmail.com");
        Assertions.assertThat(email).isEqualTo(new Email("john.doe@gmail.com"));
    }

    @Test
    void shouldNotGenerateNullValue() {
        Assertions.assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> new Email(null))
                .withMessage("Email is invalid");
    }

    @Test
    void shouldNotGenerateInvalidValue() {
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> new Email("john.doe#gmail.com"))
                .withMessage("Email is invalid");
    }

    @Test
    void shouldNotGenerateBlankValue() {
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> new Email("      "))
                .withMessage("Email is invalid");
    }

    @Test
    void shouldGenerateAndReturnToString() {
        Email email = new Email("john.doe@gmail.com");
        Assertions.assertThat(email.toString()).hasToString("john.doe@gmail.com");
    }

}