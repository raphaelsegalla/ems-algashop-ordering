package com.algaworks.algashop.ordering.domain.valueobject;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

class ShippingTest {

    @Test
    void shouldGenerate() {
        Shipping shipping = Shipping.builder()
                .cost(new Money("10.00"))
                .expectedDate(LocalDate.now().plusDays(5))
                .recipient(Recipient.builder()
                        .fullName(new FullName("John", "Doe"))
                        .document(new Document("255-08-0578"))
                        .phone(new Phone("478-256-2504"))
                        .build())
                .address(Address.builder()
                        .street("Bourbon Street")
                        .number("1134")
                        .neighborhood("North Ville")
                        .city("York")
                        .state("South California")
                        .zipCode(new ZipCode("12345"))
                        .complement("Apt. 114")
                        .build())
                .build();
        Assertions.assertThat(shipping).isEqualTo(Shipping.builder()
                .cost(new Money("10.00"))
                .expectedDate(LocalDate.now().plusDays(5))
                .recipient(Recipient.builder()
                        .fullName(new FullName("John", "Doe"))
                        .document(new Document("255-08-0578"))
                        .phone(new Phone("478-256-2504"))
                        .build())
                .address(Address.builder()
                        .street("Bourbon Street")
                        .number("1134")
                        .neighborhood("North Ville")
                        .city("York")
                        .state("South California")
                        .zipCode(new ZipCode("12345"))
                        .complement("Apt. 114")
                        .build())
                .build());
    }

    @Test
    void shouldNotEqualWhenFullNameDifference() {
        Shipping shipping = Shipping.builder()
                .cost(new Money("10.00"))
                .expectedDate(LocalDate.now().plusDays(5))
                .recipient(Recipient.builder()
                        .fullName(new FullName("John", "Doe"))
                        .document(new Document("255-08-0578"))
                        .phone(new Phone("478-256-2504"))
                        .build())
                .address(Address.builder()
                        .street("Bourbon Street")
                        .number("1134")
                        .neighborhood("North Ville")
                        .city("York")
                        .state("South California")
                        .zipCode(new ZipCode("12345"))
                        .complement("Apt. 114")
                        .build())
                .build();
        Assertions.assertThat(shipping).isNotEqualTo(Shipping.builder()
                .cost(new Money("10.00"))
                .expectedDate(LocalDate.now().plusDays(5))
                .recipient(Recipient.builder()
                        .fullName(new FullName("Raphael", "Fernando"))
                        .document(new Document("255-08-0578"))
                        .phone(new Phone("478-256-2504"))
                        .build())
                .address(Address.builder()
                        .street("Bourbon Street")
                        .number("1134")
                        .neighborhood("North Ville")
                        .city("York")
                        .state("South California")
                        .zipCode(new ZipCode("12345"))
                        .complement("Apt. 114")
                        .build())
                .build());
    }

    @Test
    void shouldNotGenerateWhenFieldNull() {
        Assertions.assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> Shipping.builder()
                        .cost(new Money("10.00"))
                        .expectedDate(LocalDate.now().plusDays(5))
                        .recipient(Recipient.builder()
                                .fullName(null)
                                .document(new Document("255-08-0578"))
                                .phone(new Phone("478-256-2504"))
                                .build())
                        .address(Address.builder()
                                .street("Bourbon Street")
                                .number("1134")
                                .neighborhood("North Ville")
                                .city("York")
                                .state("South California")
                                .zipCode(new ZipCode("12345"))
                                .complement("Apt. 114")
                                .build())
                        .build());
    }

}