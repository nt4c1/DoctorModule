package com.health.doctor.domain.model;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Set;
import java.util.UUID;

public class DoctorSchedule {
    private UUID doctorId;
    private Set<DayOfWeek> workingDays;
    private LocalTime startTime;
    private LocalTime endTime;
    private int slotDurationMinutes;
    private int maxAppointmentsPerDay;

    public DoctorSchedule(UUID doctorId, Set<DayOfWeek> workingDays, LocalTime startTime,
                          LocalTime endTime, int slotDurationMinutes, int maxAppointmentsPerDay) {
        this.doctorId = doctorId;
        this.workingDays = workingDays;
        this.startTime = startTime;
        this.endTime = endTime;
        this.slotDurationMinutes = slotDurationMinutes;
        this.maxAppointmentsPerDay = maxAppointmentsPerDay;
    }

    public boolean isWorkingDay(DayOfWeek day) {
        return workingDays.contains(day);
    }

    public boolean isValidSlot(LocalTime time) {
        return !time.isBefore(startTime) && time.plusMinutes(slotDurationMinutes).compareTo(endTime) <= 0;
    }

    public UUID getDoctorId() { return doctorId; }
    public Set<DayOfWeek> getWorkingDays() { return workingDays; }
    public LocalTime getStartTime() { return startTime; }
    public LocalTime getEndTime() { return endTime; }
    public int getSlotDurationMinutes() { return slotDurationMinutes; }
    public int getMaxAppointmentsPerDay() { return maxAppointmentsPerDay; }
}