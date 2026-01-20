package com.algaworks.algashop.ordering.domain.valueobject;

import java.util.Objects;

public record LoyaltyPoint(Integer value) implements Comparable<LoyaltyPoint> {

    public static final LoyaltyPoint ZERO = new LoyaltyPoint(0);

    public LoyaltyPoint() {
        this(0);
    }

    public LoyaltyPoint(Integer value) {
        Objects.requireNonNull(value);
        if (value < 0) {
            throw new IllegalArgumentException();
        }
        this.value = value;
    }

    public LoyaltyPoint add(Integer value) {
        return add(new LoyaltyPoint(value));
    }

    public LoyaltyPoint add(LoyaltyPoint loyaltyPoint) {
        Objects.requireNonNull(loyaltyPoint);
        if (loyaltyPoint.value() <= 0) {
            throw new IllegalArgumentException();
        }

        return new LoyaltyPoint(this.value() + loyaltyPoint.value());
    }

    @Override
    public String toString() {
        return value.toString();
    }

    @Override
    public int compareTo(LoyaltyPoint o) {
        return this.value().compareTo(o.value());
    }
}
