package com.health.patient.adapters.output.grpc;

import com.health.grpc.doctor.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import jakarta.inject.Singleton;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@Singleton
public class DoctorGrpcClient {

    private final DoctorGrpcServiceGrpc.DoctorGrpcServiceBlockingStub stub;

    public DoctorGrpcClient() {
        ManagedChannel channel = ManagedChannelBuilder
                .forAddress("localhost", 50051)
                .usePlaintext()
                .build();
        this.stub = DoctorGrpcServiceGrpc.newBlockingStub(channel);
    }

    public List<DoctorMessage> getNearbyDoctors(String locationText) {
        try {
            return stub.getNearbyDoctors(NearbyDoctorsRequest.newBuilder()
                    .setLocationText(locationText).build()).getDoctorsList();
        } catch (Exception e) {
            log.error("getNearbyDoctors error", e);
            return List.of();
        }
    }

    public List<DoctorMessage> getDoctorsByLocation(String geohashPrefix) {
        try {
            return stub.getDoctorsByLocation(DoctorsByLocationRequest.newBuilder()
                    .setGeohashPrefix(geohashPrefix).build()).getDoctorsList();
        } catch (Exception e) {
            log.error("getDoctorsByLocation error", e);
            return List.of();
        }
    }

    public GetScheduleResponse getDoctorSchedule(String doctorId) {
        try {
            return stub.getDoctorSchedule(GetScheduleRequest.newBuilder()
                    .setDoctorId(doctorId).build());
        } catch (Exception e) {
            log.error("getDoctorSchedule error", e);
            return null;
        }
    }

    public boolean createAppointment(String doctorId, String patientId,
                                     String date, String time) {
        try {
            CreateAppointmentResponse response = stub.createAppointment(
                    CreateAppointmentRequest.newBuilder()
                            .setDoctorId(doctorId).setPatientId(patientId)
                            .setDate(date).setTime(time).build()
            );
            return response.getSuccess();
        } catch (Exception e) {
            log.error("createAppointment error", e);
            return false;
        }
    }

    public boolean cancelAppointment(String appointmentId, String patientId,
                                     String doctorId, String date, String time) {
        try {
            CancelAppointmentResponse response = stub.cancelAppointment(
                    CancelAppointmentRequest.newBuilder()
                            .setAppointmentId(appointmentId).setPatientId(patientId)
                            .setDoctorId(doctorId).setDate(date).setTime(time).build()
            );
            return response.getSuccess();
        } catch (Exception e) {
            log.error("cancelAppointment error", e);
            return false;
        }
    }

    public List<AppointmentMessage> getMyAppointments(String patientId, String date) {
        try {
            return stub.getMyAppointments(GetMyAppointmentsRequest.newBuilder()
                    .setPatientId(patientId).setDate(date).build()).getAppointmentsList();
        } catch (Exception e) {
            log.error("getMyAppointments error", e);
            return List.of();
        }
    }
}