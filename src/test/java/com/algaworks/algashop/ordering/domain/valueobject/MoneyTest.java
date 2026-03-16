package com.algaworks.algashop.ordering.domain.valueobject;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

public class MoneyTest {

    @Test
    void shouldGenerate() {
        Money money = new Money(BigDecimal.TEN);
        Assertions.assertThat(money).isEqualTo(new Money("10"));
    }

    @Test
    void shouldGenerateWithTwoDecimalPlacesMinor() {
        Money money = new Money("10.00465245245");
        Assertions.assertThat(money).isEqualTo(new Money("10.00"));
    }

    @Test
    void shouldGenerateWithTwoDecimalPlacesMajor() {
        Money money = new Money("10.00765245245");
        Assertions.assertThat(money).isEqualTo(new Money("10.01"));
    }

    @Test
    void shouldNotGenerateWithNegativeValue() {
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> new Money(BigDecimal.valueOf(-10)));
    }

    @Test
    void shouldNotGenerateMoneyNullValue() {
        Assertions.assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> new Money((String) null));
    }

    @Test
    void shouldNotGenerateMoneyBlankValue() {
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> new Money("      "));
    }

    @Test
    void shouldMultiplyValue() {
        Money money = new Money(BigDecimal.TEN);
        Money multiplied = money.multiply(new Quantity(2));
        Assertions.assertThat(multiplied).isEqualTo(new Money("20"));
    }

    @Test
    void shouldNoMultiplyValueNegative() {
        Money money = new Money(BigDecimal.TEN);
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> money.multiply(new Quantity(-2)));
    }

    @Test
    void shouldAddValue() {
        Money money = new Money(BigDecimal.TEN);
        Money added = money.add(new Money("2"));
        Assertions.assertThat(added).isEqualTo(new Money("12"));
    }

    @Test
    void shouldNotAddMoneyNullValue() {
        Money money = new Money(BigDecimal.TEN);
        Assertions.assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> money.add(null));
    }

    @Test
    void shouldCompareValues() {
        Money money = new Money(BigDecimal.TEN);
        Money moneyToCompare = new Money("12");
        int compare = money.compareTo(moneyToCompare);
        Assertions.assertThat(compare).isEqualTo(-1);
    }

    @Test
    void shouldGenerateAndReturnToString() {
        Money money = new Money(BigDecimal.TEN);
        Assertions.assertThat(money.toString()).isEqualTo("10.00");
    }

    @Test
    void shouldDivideValues() {
        Money money = new Money(BigDecimal.TEN);
        Money divided = money.divide(new Money("2"));
        Assertions.assertThat(divided).isEqualTo(new Money("5.00"));
    }

    @Test
    void shouldDivideValuesWithTwoDecimalPlacesMinor() {
        Money money = new Money(BigDecimal.TEN);
        Money divided = money.divide(new Money("3"));
        Assertions.assertThat(divided).isEqualTo(new Money("3.33"));
    }

    @Test
    void shouldDivideValuesWithTwoDecimalPlacesMajor() {
        Money money = new Money(BigDecimal.TEN);
        Money divided = money.divide(new Money("6"));
        Assertions.assertThat(divided).isEqualTo(new Money("1.67"));
    }

    @Test
    void shouldDivideZeroValue() {
        Money money = new Money(BigDecimal.TEN);
        Assertions.assertThatExceptionOfType(ArithmeticException.class)
                .isThrownBy(() -> money.divide(new Money("0")));
    }

}
