package com.flashsale.flashsale_engine.model;

public enum OrderStatus {
    PENDING,      // order placed, payment not confirmed yet
    CONFIRMED,    // payment confirmed, order is good
    CANCELLED     // order cancelled (payment failed / user cancelled)
}