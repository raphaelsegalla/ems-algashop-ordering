package com.algaworks.algashop.ordering.domain.valueobject;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class BillingInfoTest {

    @Test
    void shouldGenerate() {
        BillingInfo billingInfo = BillingInfo.builder()
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
        Assertions.assertThat(billingInfo).isEqualTo(BillingInfo.builder()
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
        BillingInfo billingInfo = BillingInfo.builder()
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
        Assertions.assertThat(billingInfo).isNotEqualTo(BillingInfo.builder()
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
                .isThrownBy(() -> BillingInfo.builder()
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