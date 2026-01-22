package com.algaworks.algashop.ordering.domain.valueobject;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class DocumentTest {

    @Test
    void shouldGenerate() {
        Document document = new Document("255-08-0578");
        Assertions.assertThat(document).isEqualTo(new Document("255-08-0578"));
    }

}