package com.health.doctor.adapters.input.grpc;

import com.health.doctor.application.usecase.implementtion.*;
import com.health.doctor.application.usecase.interfaces.*;
import com.health.doctor.domain.model.*;
import com.health.doctor.domain.ports.AppointmentRepositoryPort;
import com.health.doctor.domain.ports.DoctorRepositoryPort;
import com.health.grpc.doctor.*;
import io.grpc.stub.StreamObserver;
import jakarta.inject.Singleton;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Singleton
public class DoctorGrpcServer extends DoctorGrpcServiceGrpc.DoctorGrpcServiceImplBase {

    private final GetDoctorsByLocationUseCaseInterface locationUseCase;
    private final GetScheduleUseCaseInterface scheduleUseCase;
    private final GetAppointmentsUseCaseInterface appointmentsUseCase;
    private final GetPendingAppointmentsUseCaseInterface pendingUseCase;
    private final AcceptAppointmentUseCaseInterface acceptUseCase;
    private final PostponeAppointmentUseCaseInterface postponeUseCase;
    private final UpdateDoctorLocationUseCaseInterface updateLocationUseCase;
    private final CreateAppointmentUseCaseInterface createAppointmentUseCase;
    private final DoctorRepositoryPort doctorRepo;
    private final AppointmentRepositoryPort appointmentRepo;

    public DoctorGrpcServer(GetDoctorsByLocationUseCase locationUseCase,
                            GetScheduleUseCase scheduleUseCase,
                            GetAppointmentsUseCase appointmentsUseCase,
                            GetPendingAppointmentsUseCase pendingUseCase,
                            AcceptAppointmentUseCase acceptUseCase,
                            PostponeAppointmentUseCase postponeUseCase,
                            UpdateDoctorLocationUseCase updateLocationUseCase,
                            CreateAppointmentUseCase createAppointmentUseCase,
                            DoctorRepositoryPort doctorRepo,
                            AppointmentRepositoryPort appointmentRepo) {
        this.locationUseCase = locationUseCase;
        this.scheduleUseCase = scheduleUseCase;
        this.appointmentsUseCase = appointmentsUseCase;
        this.pendingUseCase = pendingUseCase;
        this.acceptUseCase = acceptUseCase;
        this.postponeUseCase = postponeUseCase;
        this.updateLocationUseCase = updateLocationUseCase;
        this.createAppointmentUseCase = createAppointmentUseCase;
        this.doctorRepo = doctorRepo;
        this.appointmentRepo = appointmentRepo;
    }

    @Override
    public void getNearbyDoctors(NearbyDoctorsRequest request,
                                 StreamObserver<NearbyDoctorsResponse> responseObserver) {
        try {
            List<Doctor> doctors = locationUseCase.executeNear(request.getLocationText());
            responseObserver.onNext(NearbyDoctorsResponse.newBuilder()
                    .addAllDoctors(doctors.stream().map(this::toMsg).collect(Collectors.toList()))
                    .build());
            responseObserver.onCompleted();
        } catch (Exception e) {
            log.error("getNearbyDoctors error", e);
            responseObserver.onError(e);
        }
    }

    @Override
    public void getDoctorsByLocation(DoctorsByLocationRequest request,
                                     StreamObserver<DoctorsByLocationResponse> responseObserver) {
        try {
            List<Doctor> doctors = locationUseCase.execute(request.getGeohashPrefix());
            responseObserver.onNext(DoctorsByLocationResponse.newBuilder()
                    .addAllDoctors(doctors.stream().map(this::toMsg).collect(Collectors.toList()))
                    .build());
            responseObserver.onCompleted();
        } catch (Exception e) {
            log.error("getDoctorsByLocation error", e);
            responseObserver.onError(e);
        }
    }

    @Override
    public void getDoctorSchedule(GetScheduleRequest request,
                                  StreamObserver<GetScheduleResponse> responseObserver) {
        try {
            DoctorSchedule s = scheduleUseCase.execute(UUID.fromString(request.getDoctorId()));
            responseObserver.onNext(GetScheduleResponse.newBuilder()
                    .setDoctorId(s.getDoctorId().toString())
                    .addAllWorkingDays(s.getWorkingDays().stream()
                            .map(Enum::name).collect(Collectors.toList()))
                    .setStartTime(s.getStartTime().toString())
                    .setEndTime(s.getEndTime().toString())
                    .setSlotDurationMinutes(s.getSlotDurationMinutes())
                    .setMaxAppointmentsDay(s.getMaxAppointmentsPerDay())
                    .build());
            responseObserver.onCompleted();
        } catch (Exception e) {
            log.error("getDoctorSchedule error", e);
            responseObserver.onError(e);
        }
    }

    @Override
    public void getAppointments(GetAppointmentsRequest request,
                                StreamObserver<GetAppointmentsResponse> responseObserver) {
        try {
            List<Appointment> list = appointmentsUseCase.execute(
                    UUID.fromString(request.getDoctorId()),
                    LocalDate.parse(request.getDate())
            );
            responseObserver.onNext(GetAppointmentsResponse.newBuilder()
                    .addAllAppointments(list.stream().map(this::toApptMsg).collect(Collectors.toList()))
                    .build());
            responseObserver.onCompleted();
        } catch (Exception e) {
            log.error("getAppointments error", e);
            responseObserver.onError(e);
        }
    }

    @Override
    public void getPendingAppointments(GetAppointmentsRequest request,
                                       StreamObserver<GetAppointmentsResponse> responseObserver) {
        try {
            List<Appointment> list = pendingUseCase.execute(
                    UUID.fromString(request.getDoctorId()),
                    LocalDate.parse(request.getDate())
            );
            responseObserver.onNext(GetAppointmentsResponse.newBuilder()
                    .addAllAppointments(list.stream().map(this::toApptMsg).collect(Collectors.toList()))
                    .build());
            responseObserver.onCompleted();
        } catch (Exception e) {
            log.error("getPendingAppointments error", e);
            responseObserver.onError(e);
        }
    }

    @Override
    public void acceptAppointment(AppointmentActionRequest request,
                                  StreamObserver<AppointmentActionResponse> responseObserver) {
        try {
            acceptUseCase.execute(toAppointment(request));
            responseObserver.onNext(AppointmentActionResponse.newBuilder()
                    .setSuccess(true).setMessage("Accepted").build());
            responseObserver.onCompleted();
        } catch (Exception e) {
            log.error("acceptAppointment error", e);
            responseObserver.onNext(AppointmentActionResponse.newBuilder()
                    .setSuccess(false).setMessage(e.getMessage()).build());
            responseObserver.onCompleted();
        }
    }

    @Override
    public void postponeAppointment(AppointmentActionRequest request,
                                    StreamObserver<AppointmentActionResponse> responseObserver) {
        try {
            postponeUseCase.execute(toAppointment(request));
            responseObserver.onNext(AppointmentActionResponse.newBuilder()
                    .setSuccess(true).setMessage("Postponed").build());
            responseObserver.onCompleted();
        } catch (Exception e) {
            log.error("postponeAppointment error", e);
            responseObserver.onNext(AppointmentActionResponse.newBuilder()
                    .setSuccess(false).setMessage(e.getMessage()).build());
            responseObserver.onCompleted();
        }
    }

    @Override
    public void updateLocation(UpdateLocationRequest request,
                               StreamObserver<UpdateLocationResponse> responseObserver) {
        try {
            updateLocationUseCase.execute(
                    UUID.fromString(request.getDoctorId()),
                    request.getLocationText()
            );
            responseObserver.onNext(UpdateLocationResponse.newBuilder()
                    .setSuccess(true).build());
            responseObserver.onCompleted();
        } catch (Exception e) {
            log.error("updateLocation error", e);
            responseObserver.onNext(UpdateLocationResponse.newBuilder()
                    .setSuccess(false).build());
            responseObserver.onCompleted();
        }
    }

    @Override
    public void doctorExists(DoctorExistsRequest request,
                             StreamObserver<DoctorExistsResponse> responseObserver) {
        try {
            Doctor d = doctorRepo.findById(UUID.fromString(request.getDoctorId()));
            responseObserver.onNext(DoctorExistsResponse.newBuilder()
                    .setExists(d != null).build());
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onNext(DoctorExistsResponse.newBuilder()
                    .setExists(false).build());
            responseObserver.onCompleted();
        }
    }

    @Override
    public void createAppointment(CreateAppointmentRequest request,
                                  StreamObserver<CreateAppointmentResponse> responseObserver) {
        try {
            createAppointmentUseCase.execute(
                    UUID.fromString(request.getDoctorId()),
                    UUID.fromString(request.getPatientId()),
                    LocalDate.parse(request.getDate()),
                    LocalTime.parse(request.getTime())
            );
            responseObserver.onNext(CreateAppointmentResponse.newBuilder()
                    .setSuccess(true).setMessage("Appointment created").build());
            responseObserver.onCompleted();
        } catch (Exception e) {
            log.error("createAppointment error", e);
            responseObserver.onNext(CreateAppointmentResponse.newBuilder()
                    .setSuccess(false).setMessage(e.getMessage()).build());
            responseObserver.onCompleted();
        }
    }

    @Override
    public void cancelAppointment(CancelAppointmentRequest request,
                                  StreamObserver<CancelAppointmentResponse> responseObserver) {
        try {
            appointmentRepo.cancel(
                    UUID.fromString(request.getAppointmentId()),
                    UUID.fromString(request.getPatientId()),
                    UUID.fromString(request.getDoctorId()),
                    LocalDate.parse(request.getDate()),
                    LocalTime.parse(request.getTime())
            );
            responseObserver.onNext(CancelAppointmentResponse.newBuilder()
                    .setSuccess(true).setMessage("Cancelled").build());
            responseObserver.onCompleted();
        } catch (Exception e) {
            log.error("cancelAppointment error", e);
            responseObserver.onNext(CancelAppointmentResponse.newBuilder()
                    .setSuccess(false).setMessage(e.getMessage()).build());
            responseObserver.onCompleted();
        }
    }

    @Override
    public void getMyAppointments(GetMyAppointmentsRequest request,
                                  StreamObserver<GetMyAppointmentsResponse> responseObserver) {
        try {
            List<Appointment> list = appointmentRepo.findByPatientAndDate(
                    UUID.fromString(request.getPatientId()),
                    LocalDate.parse(request.getDate())
            );
            responseObserver.onNext(GetMyAppointmentsResponse.newBuilder()
                    .addAllAppointments(list.stream().map(this::toApptMsg).collect(Collectors.toList()))
                    .build());
            responseObserver.onCompleted();
        } catch (Exception e) {
            log.error("getMyAppointments error", e);
            responseObserver.onError(e);
        }
    }

    private DoctorMessage toMsg(Doctor d) {
        return DoctorMessage.newBuilder()
                .setDoctorId(d.getId() != null ? d.getId().toString() : "")
                .setName(d.getName() != null ? d.getName() : "")
                .setType(d.getType() != null ? d.getType().name() : "")
                .setSpecialization(d.getSpecialization() != null ? d.getSpecialization() : "")
                .setIsActive(d.isActive())
                .build();
    }

    private AppointmentMessage toApptMsg(Appointment a) {
        return AppointmentMessage.newBuilder()
                .setAppointmentId(a.getId().toString())
                .setDoctorId(a.getDoctorId().toString())
                .setPatientId(a.getPatientId().toString())
                .setDate(a.getAppointmentDate().toString())
                .setTime(a.getScheduleTime().toString())
                .setStatus(a.getStatus().name())
                .build();
    }

    private Appointment toAppointment(AppointmentActionRequest r) {
        return new Appointment(
                UUID.fromString(r.getAppointmentId()),
                UUID.fromString(r.getDoctorId()),
                UUID.fromString(r.getPatientId()),
                LocalDate.parse(r.getDate()),
                LocalTime.parse(r.getTime()),
                AppointmentStatus.valueOf(r.getCurrentStatus()),
                null, null
        );
    }
}