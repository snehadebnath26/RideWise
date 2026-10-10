package com.airtribe.ridewise.util;

import java.util.concurrent.atomic.AtomicLong;

public final class IdGenerator {
    private static final AtomicLong RIDER_SEQUENCE = new AtomicLong(0);
    private static final AtomicLong DRIVER_SEQUENCE = new AtomicLong(0);
    private static final AtomicLong RIDE_SEQUENCE = new AtomicLong(0);

    private IdGenerator() {
    }

    public static String nextRiderId() {
        return "R" + RIDER_SEQUENCE.incrementAndGet();
    }

    public static String nextDriverId() {
        return "D" + DRIVER_SEQUENCE.incrementAndGet();
    }

    public static String nextRideId() {
        return "RW" + RIDE_SEQUENCE.incrementAndGet();
    }
}
