package com.algaworks.algashop.ordering.domain.entity;

import com.algaworks.algashop.ordering.domain.exception.CustomerArchivedException;
import com.algaworks.algashop.ordering.domain.valueobject.*;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class CustomerTest {

    @Test
    void given_invalidEmail_whenTryCreateCustomer_ShouldGenerateException() {

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> {
                    CustomerTestDataBuilder.brandNewCustomer()
                            .email(new Email("invalid"))
                            .build();
                });
    }

    @Test
    void given_invalidEmail_whenTryUpdatedCreateCustomerEmail_ShouldGenerateException() {
        Customer customer = CustomerTestDataBuilder.brandNewCustomer().build();

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> {
                    customer.changeEmail(new Email("invalid"));
                });
    }

    @Test
    void given_unarchivedCustomer_whenArchive_ShouldAnonymize() {
        Customer customer = CustomerTestDataBuilder.existingCustomer().build();

        customer.archive();

        assertWith(customer,
                    c -> assertThat(c.fullName()).isEqualTo(new FullName("Anonymous", "Anonymous")),
                    c -> assertThat(c.email()).isNotEqualTo(new Email("john.doe@gmail.com")),
                    c -> assertThat(c.phone()).isEqualTo(new Phone("000-000-0000")),
                    c -> assertThat(c.document()).isEqualTo(new Document("000-00-0000")),
                    c -> assertThat(c.birthDate()).isNull(),
                    c -> assertThat(c.isPromotionNotificationsAllowed()).isFalse(),
                    c -> assertThat(c.address()).isEqualTo(
                            Address.builder()
                                    .street("Bourbon Street")
                                    .number("Anonymized")
                                    .neighborhood("North Ville")
                                    .city("York")
                                    .state("South California")
                                    .zipCode(new ZipCode("12345"))
                                    .complement(null)
                                    .build()
                    )
                );
    }

    @Test
    void given_archivedCustomer_whenTryToUpdate_ShouldGenerateException() {
        Customer customer = CustomerTestDataBuilder.existingAnonymizedCustomer().build();

        assertThatExceptionOfType(CustomerArchivedException.class)
                .isThrownBy(customer::archive);
        assertThatExceptionOfType(CustomerArchivedException.class)
                .isThrownBy(() -> customer.changeEmail(new Email("email@gmail.com")));
        assertThatExceptionOfType(CustomerArchivedException.class)
                .isThrownBy(() -> customer.changePhone(new Phone("123-123-1111")));
        assertThatExceptionOfType(CustomerArchivedException.class)
                .isThrownBy(customer::enablePromotionNotifications);
        assertThatExceptionOfType(CustomerArchivedException.class)
                .isThrownBy(customer::disablePromotionNotifications);
    }

    @Test
    void given_brandNewCustomer_whenAddLoyaltyPoints_ShouldSumPoints() {
        Customer customer = CustomerTestDataBuilder.brandNewCustomer().build();

        customer.addLoyaltyPoints(new LoyaltyPoint(20));
        customer.addLoyaltyPoints(new LoyaltyPoint(10));

        assertThat(customer.loyaltyPoints()).isEqualTo(new LoyaltyPoint(30));

    }

    @Test
    void given_brandNewCustomer_whenAddInvalidLoyaltyPoints_ShouldGenerationException() {
        Customer customer = CustomerTestDataBuilder.brandNewCustomer().build();

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> customer.addLoyaltyPoints(new LoyaltyPoint(0)));

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> customer.addLoyaltyPoints(new LoyaltyPoint(-10)));

    }

}