package com.algaworks.algashop.ordering.domain.valueobject;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

class BirthDateTest {

    @Test
    void shouldGenerate() {
        BirthDate birthDate = new BirthDate(LocalDate.of(1991, 7, 5));
        Assertions.assertThat(birthDate.age()).isEqualTo(34);
    }

}