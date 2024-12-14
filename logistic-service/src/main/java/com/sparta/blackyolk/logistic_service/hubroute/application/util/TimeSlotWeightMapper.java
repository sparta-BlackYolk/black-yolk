package com.sparta.blackyolk.logistic_service.hubroute.application.util;

import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class TimeSlotWeightMapper {

    private static final Map<String, Double> TIME_SLOT_WEIGHTS;

    static {
        TIME_SLOT_WEIGHTS = Map.of(
            "22:00-05:00", 1.0,
            "05:00-07:00", 1.2,
            "07:00-09:00", 2.0,
            "09:00-17:00", 1.5,
            "17:00-20:00", 2.0,
            "20:00-22:00", 1.2
        );
    }

    public double getWeight(String timeSlot) {
        return TIME_SLOT_WEIGHTS.getOrDefault(timeSlot, 1.0);
    }

    public Map<String, Double> getTimeSlots() {
        return TIME_SLOT_WEIGHTS;
    }
}
