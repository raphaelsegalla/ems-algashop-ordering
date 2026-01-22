package com.algaworks.algashop.ordering.domain.valueobject;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class PhoneTest {

    @Test
    void shouldGenerate() {
        Phone phone = new Phone("478-256-2504");
        Assertions.assertThat(phone).isEqualTo(new Phone("478-256-2504"));
    }

}