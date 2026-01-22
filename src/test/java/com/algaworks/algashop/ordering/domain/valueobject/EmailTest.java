package com.algaworks.algashop.ordering.domain.valueobject;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class EmailTest {

    @Test
    void shouldGenerate() {
        Email email = new Email("john.doe@gmail.com");
        Assertions.assertThat(email).isEqualTo(new Email("john.doe@gmail.com"));
    }

}