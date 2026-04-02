cqlsh -f schema.cql
```

---

### Step 3 — Run Doctor Module in IntelliJ

1. Open `DoctorModule/src/main/java/com/health/doctor/Application.java`
2. Right-click → **Run 'Application'**
3. Wait for:
```
Server Running: http://localhost:8080
GRPC started on port 50051
```

---

### Step 4 — Run Patient Module in IntelliJ

1. Open `PatientModule/src/main/java/com/health/patient/Application.java`
2. Right-click → **Run 'Application'**
3. Wait for:
```
Server Running: http://localhost:8081
GRPC started on port 50053
```

Both must be running at the same time — they run as separate processes.

---

## Postman Testing — Exact Order

### 1. Create Clinic (Doctor Module)
```
POST http://localhost:8080/doctor/cclinic
Content-Type: application/json

{
"name": "Kathmandu Clinic",
"locationText": "Kathmandu"
}
```
→ Save the returned `clinicId`

---

### 2. Create Doctor (Doctor Module)
```
POST http://localhost:8080/doctor/cdoctor
Content-Type: application/json

{
"name": "Dr. Ram",
"clinicId":"From the Above,
"specialization": "Physian",
"type": "CLINIC_DOCTOR"
}
```

else for individual 
{
    "name": "Dr. Ram",
    "clinicId":"",
    "specialization": "Physian",
    "type": "INDIVIDUAL"
}

→ Save the returned `doctorId`

---

### 3. Update Doctor Location (Doctor Module)
```
POST http://localhost:8080/doctor/<doctorId>/location
Content-Type: application/json

{
"locationText": "Kathmandu"
}
```

---

### 4. Create Doctor Schedule (Doctor Module)
```
POST http://localhost:8080/doctor/schedule
Content-Type: application/json

{
"doctorId": "<doctorId>",
"workingDays": ["MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY"],
"startTime": "09:00:00",
"endTime": "17:00:00",
"slotDurationMinutes": 30,
"maxAppointmentsPerDay": 50
}
```

---

### 5. Create Patient (Patient Module)
```
POST http://localhost:8081/patient
Content-Type: application/json

{
"name": "Ram Bahadur"
}
```
→ Save the returned `patientId`

---

### 6. Get Nearby Doctors (Patient Module → calls Doctor via gRPC)
```
GET http://localhost:8081/patient/doctors/nearby?location=Kathmandu
```
→ Should return list of doctors

---

### 7. Get Doctor Schedule (Patient Module → calls Doctor via gRPC)
```
GET http://localhost:8081/patient/doctors/<doctorId>/schedule
```

---

### 8. Book Appointment (Patient Module → calls Doctor via gRPC)
```
POST http://localhost:8081/patient/appointments
Content-Type: application/json

{
"doctorId": "<doctorId>",
"patientId": "<patientId>",
"date": "2026-04-07",
"time": "10:00:00"
}
```
Note: `2026-04-07` is a Monday — must match working days in schedule.

---

### 9. Get My Appointments (Patient Module → calls Doctor via gRPC)
```
GET http://localhost:8081/patient/appointments/<patientId>?date=2026-04-07
```

---

### 10. Get Doctor's Appointments (Doctor Module)
```
GET http://localhost:8080/appointments/<doctorId>?date=2026-04-07
```

---

### 11. Get Pending Appointments (Doctor Module)
```
GET http://localhost:8080/appointments/<doctorId>/pending?date=2026-04-07
```

---

### 12. Accept Appointment (Doctor Module)
```
POST http://localhost:8080/appointments/status/accept
Content-Type: application/json

{
"appointmentId": "<appointmentId>",
"doctorId": "<doctorId>",
"patientId": "<patientId>",
"date": "2026-04-07",
"time": "10:00:00",
"currentStatus": "PENDING"
}
```

---

### 13. Postpone Appointment (Doctor Module)
```
POST http://localhost:8080/appointments/status/postpone
Content-Type: application/json

{
"appointmentId": "<appointmentId>",
"doctorId": "<doctorId>",
"patientId": "<patientId>",
"date": "2026-04-07",
"time": "10:00:00",
"currentStatus": "PENDING"
}
```
→ Date will automatically shift to `2026-04-08`

---

### 14. Cancel Appointment (Patient Module → calls Doctor via gRPC)
```
DELETE http://localhost:8081/patient/appointments/<appointmentId>?patientId=<patientId>&doctorId=<doctorId>&date=2026-04-07&time=10:00:00
```

---

### 15. Get Doctors in Same Clinic (Doctor Module)
```
GET http://localhost:8080/doctor/clinic/<clinicId>