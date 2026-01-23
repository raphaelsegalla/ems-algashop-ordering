package com.algaworks.algashop.ordering.domain.valueobject;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

class DocumentTest {

    @Test
    void shouldGenerate() {
        Document document = new Document("255-08-0578");
        Assertions.assertThat(document).isEqualTo(new Document("255-08-0578"));
    }

    @Test
    void shouldNotGenerateNullValue() {
        Assertions.assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> new Document(null));
    }

    @Test
    void shouldNotGenerateBlankValue() {
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> new Document("     "));
    }

    @Test
    void shouldGenerateAndReturnToString() {
        Document document = new Document("255-08-0578");
        Assertions.assertThat(document.toString()).hasToString("255-08-0578");
    }

}