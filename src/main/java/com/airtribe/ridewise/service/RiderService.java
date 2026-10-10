package com.airtribe.ridewise.service;

import com.airtribe.ridewise.exception.EntityNotFoundException;
import com.airtribe.ridewise.model.Location;
import com.airtribe.ridewise.model.Rider;
import com.airtribe.ridewise.util.IdGenerator;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public class RiderService {
    private final Map<String, Rider> riders = new LinkedHashMap<>();

    public Rider registerRider(String name, Location location) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Rider name cannot be empty");
        }
        Rider rider = new Rider(IdGenerator.nextRiderId(), name.trim(), location);
        riders.put(rider.getId(), rider);
        return rider;
    }

    public Rider getRiderById(String id) {
        Rider rider = riders.get(id);
        if (rider == null) {
            throw new EntityNotFoundException("Rider not found: " + id);
        }
        return rider;
    }

    public Collection<Rider> getAllRiders() {
        return ListCopy.copy(riders.values());
    }

    private static final class ListCopy {
        private static <T> Collection<T> copy(Collection<T> values) {
            return java.util.List.copyOf(values);
        }
    }
}
