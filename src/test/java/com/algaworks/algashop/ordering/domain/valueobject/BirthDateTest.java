package com.algaworks.algashop.ordering.domain.valueobject;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

class BirthDateTest {

    @Test
    void shouldGenerate() {
        BirthDate birthDate = new BirthDate(LocalDate.of(1991, 7, 5));
        Assertions.assertThat(birthDate).isEqualTo(new BirthDate(LocalDate.of(1991, 7, 5)));
    }

    @Test
    void shouldNotGenerateNullValue() {
        Assertions.assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> new BirthDate(null));
    }

    @Test
    void shouldNotGenerateDateBeforeValue() {
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> new BirthDate(LocalDate.now().plusDays(1)));
    }

    @Test
    void shouldReturnAge() {
        BirthDate birthDate = new BirthDate(LocalDate.of(1991, 7, 5));
        Assertions.assertThat(birthDate.age()).isEqualTo(34);
    }

    @Test
    void shouldGenerateAndReturnToString() {
        BirthDate birthDate = new BirthDate(LocalDate.of(1991, 7, 5));
        Assertions.assertThat(birthDate.toString()).hasToString("1991-07-05");
    }

}