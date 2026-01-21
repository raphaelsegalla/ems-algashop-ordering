package com.algaworks.algashop.ordering.domain.entity;

import com.algaworks.algashop.ordering.domain.exception.CustomerArchivedException;
import com.algaworks.algashop.ordering.domain.valueobject.BirthDate;
import com.algaworks.algashop.ordering.domain.valueobject.CustomerId;
import com.algaworks.algashop.ordering.domain.valueobject.Document;
import com.algaworks.algashop.ordering.domain.valueobject.FullName;
import com.algaworks.algashop.ordering.domain.valueobject.LoyaltyPoint;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

class CustomerTest {

    @Test
    void given_invalidEmail_whenTryCreateCustomer_ShouldGenerateException() {

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> {
                    new Customer(
                            new CustomerId(),
                            new FullName("John", "Doe"),
                            new BirthDate(LocalDate.of(1991, 7, 5)),
                            "invalid",
                            "478-256-2504",
                            new Document("255-08-0578"),
                            false,
                            OffsetDateTime.now()
                    );
                });
    }

    @Test
    void given_invalidEmail_whenTryUpdatedCreateCustomerEmail_ShouldGenerateException() {
        Customer customer = new Customer(
                new CustomerId(),
                new FullName("John", "Doe"),
                new BirthDate(LocalDate.of(1991, 7, 5)),
                "john.doe@gmail.com",
                "478-256-2504",
                new Document("255-08-0578"),
                false,
                OffsetDateTime.now()
        );

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> {
                    customer.changeEmail("invalid");
                });
    }

    @Test
    void given_unarchivedCustomer_whenArchive_ShouldAnonymize() {
        Customer customer = new Customer(
                new CustomerId(),
                new FullName("John", "Doe"),
                new BirthDate(LocalDate.of(1991, 7, 5)),
                "john.doe@gmail.com",
                "478-256-2504",
                new Document("255-08-0578"),
                false,
                OffsetDateTime.now()
        );

        customer.archive();

        assertWith(customer,
                    c -> assertThat(c.fullName()).isEqualTo(new FullName("Anonymous", "Anonymous")),
                    c -> assertThat(c.email()).isNotEqualTo("john.doe@gmail.com"),
                    c -> assertThat(c.phone()).isEqualTo("000-000-0000"),
                    c -> assertThat(c.document()).isEqualTo(new Document("000-00-0000")),
                    c -> assertThat(c.birthDate()).isNull(),
                    c -> assertThat(c.isPromotionNotificationsAllowed()).isFalse()
                );
    }

    @Test
    void given_archivedCustomer_whenTryToUpdate_ShouldGenerateException() {
        Customer customer = new Customer(
                new CustomerId(),
                new FullName("Anonymous", "Anonymous"),
                null,
                UUID.randomUUID() + "@anounymous.com",
                "000-000-0000",
                new Document("000-00-0000"),
                false,
                true,
                OffsetDateTime.now(),
                OffsetDateTime.now(),
                new LoyaltyPoint(10)
        );

        assertThatExceptionOfType(CustomerArchivedException.class)
                .isThrownBy(customer::archive);
        assertThatExceptionOfType(CustomerArchivedException.class)
                .isThrownBy(() -> customer.changeEmail("email@gmail.com"));
        assertThatExceptionOfType(CustomerArchivedException.class)
                .isThrownBy(() -> customer.changePhone("123-123-1111"));
        assertThatExceptionOfType(CustomerArchivedException.class)
                .isThrownBy(customer::enablePromotionNotifications);
        assertThatExceptionOfType(CustomerArchivedException.class)
                .isThrownBy(customer::disablePromotionNotifications);
    }

    @Test
    void given_brandNewCustomer_whenAddLoyaltyPoints_ShouldSumPoints() {
        Customer customer = new Customer(
                new CustomerId(),
                new FullName("John", "Doe"),
                new BirthDate(LocalDate.of(1991, 7, 5)),
                "john.doe@gmail.com",
                "478-256-2504",
                new Document("255-08-0578"),
                false,
                OffsetDateTime.now()
        );

        customer.addLoyaltyPoints(new LoyaltyPoint(20));
        customer.addLoyaltyPoints(new LoyaltyPoint(10));

        assertThat(customer.loyaltyPoints()).isEqualTo(new LoyaltyPoint(30));

    }

    @Test
    void given_brandNewCustomer_whenAddInvalidLoyaltyPoints_ShouldGenerationException() {
        Customer customer = new Customer(
                new CustomerId(),
                new FullName("John", "Doe"),
                new BirthDate(LocalDate.of(1991, 7, 5)),
                "john.doe@gmail.com",
                "478-256-2504",
                new Document("255-08-0578"),
                false,
                OffsetDateTime.now()
        );

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> customer.addLoyaltyPoints(new LoyaltyPoint(0)));

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> customer.addLoyaltyPoints(new LoyaltyPoint(-10)));

    }

}