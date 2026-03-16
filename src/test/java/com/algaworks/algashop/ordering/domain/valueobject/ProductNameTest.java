package com.algaworks.algashop.ordering.domain.valueobject;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class ProductNameTest {

    @Test
    void shouldGenerate() {
        ProductName productName = new ProductName("PlayStation 5");
        Assertions.assertThat(productName).isEqualTo(new ProductName("PlayStation 5"));
    }

    @Test
    void shouldNotGenerateProductNameNullValue() {
        Assertions.assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> new ProductName(null));
    }

    @Test
    void shouldNotGenerateProductNameBlankValue() {
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> new ProductName("      "));
    }

    @Test
    void shouldGenerateAndReturnToString() {
        ProductName productName = new ProductName("PlayStation 5");
        Assertions.assertThat(productName.toString()).hasToString("PlayStation 5");
    }

}
