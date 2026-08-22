package com.algaworks.algashop.ordering.domain.valueobject;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class BillingTest {

    @Test
    void shouldGenerate() {
        Billing billing = Billing.builder()
                .fullName(new FullName("John", "Doe"))
                .document(new Document("255-08-0578"))
                .phone(new Phone("478-256-2504"))
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
        Assertions.assertThat(billing).isEqualTo(Billing.builder()
                .fullName(new FullName("John", "Doe"))
                .document(new Document("255-08-0578"))
                .phone(new Phone("478-256-2504"))
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
        Billing billing = Billing.builder()
                .fullName(new FullName("John", "Doe"))
                .document(new Document("255-08-0578"))
                .phone(new Phone("478-256-2504"))
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
        Assertions.assertThat(billing).isNotEqualTo(Billing.builder()
                .fullName(new FullName("Raphael", "Fernando"))
                .document(new Document("255-08-0578"))
                .phone(new Phone("478-256-2504"))
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
                .isThrownBy(() -> Billing.builder()
                        .fullName(null)
                        .document(new Document("255-08-0578"))
                        .phone(new Phone("478-256-2504"))
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