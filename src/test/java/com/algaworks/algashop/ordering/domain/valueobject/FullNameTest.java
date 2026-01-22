package com.algaworks.algashop.ordering.domain.valueobject;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class FullNameTest {

    @Test
    void shouldGenerate() {
        FullName fullName = new FullName("John", "Doe");
        Assertions.assertThat(fullName).isEqualTo(new FullName("John", "Doe"));
    }

}