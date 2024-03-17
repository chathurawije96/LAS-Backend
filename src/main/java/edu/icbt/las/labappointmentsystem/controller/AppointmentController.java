package edu.icbt.las.labappointmentsystem.controller;

import edu.icbt.las.labappointmentsystem.domain.Appointment;
import edu.icbt.las.labappointmentsystem.domain.Payment;
import edu.icbt.las.labappointmentsystem.domain.Report;
import edu.icbt.las.labappointmentsystem.dto.*;
import edu.icbt.las.labappointmentsystem.dto.common.ErrorResponse;
import edu.icbt.las.labappointmentsystem.exception.ServiceException;
import edu.icbt.las.labappointmentsystem.service.AppointmentService;
import edu.icbt.las.labappointmentsystem.service.PaymentService;
import edu.icbt.las.labappointmentsystem.service.ReportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {
    @Autowired
    private AppointmentService appointmentService;
    @Autowired
    private PaymentService paymentService;
    @Autowired
    private ReportService reportService;

    @GetMapping("/")
    @PreAuthorize("hasAuthority('PATIENT')")
    public ResponseEntity getAllAppointmentsByLoggedUser() {
        try {
            return ResponseEntity.ok(mapAppointmentResponse(appointmentService.getAllByLoggedUser(getLoggedUser())));
        } catch (ServiceException e) {
            ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('LAB_OPERATOR')")
    public ResponseEntity getAllAppointments() {
        try {
            return ResponseEntity.ok(mapAppointmentResponseForALl(appointmentService.findAll()));
        } catch (ServiceException e) {
            ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    @GetMapping("/tests/{id}")
    @PreAuthorize("hasAuthority('PATIENT')")
    public ResponseEntity getAllAppointmentTests(@PathVariable("id") long appointmentId) {
        try {
            return ResponseEntity.ok(mapAppointmentTestsResponse(appointmentService.findById(appointmentId)));
        } catch (ServiceException e) {
            ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    private List<AppointmentTestResponse> mapAppointmentTestsResponse(Optional<Appointment> byId) {
        List<AppointmentTestResponse> responses = new ArrayList<>();
        byId.ifPresent(appointment -> appointment.getAppointmentTests().forEach(appointmentTests -> {
            Report report = null;
            try {
                report = reportService.findReportByAppointmentTest(appointmentTests.getId());
            } catch (ServiceException e) {
                log.debug("error {} {}", e.getMessage(),e);
            }
            responses.add(AppointmentTestResponse.builder()
                    .testId(appointmentTests.getId())
                    .testName(appointmentTests.getTest().getName())
                    .testShortName(appointmentTests.getTest().getShortName())
                    .report(report!= null ? report.getReport() : null)
                    .status(appointmentTests.getStatus().name())
                    .build());
        }));
        return responses;
    }

    @PostMapping("/")
    @PreAuthorize("hasAuthority('PATIENT')")
    public @ResponseBody ResponseEntity makeAppointments(@RequestBody MakeAppointmentRequest makeAppointmentRequest) {
        try {
            return ResponseEntity.ok(appointmentService.makeAppointment(makeAppointmentRequest, getLoggedUser()));
        } catch (ServiceException e) {
            ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    @PutMapping("/payment/{id}")
    @PreAuthorize("hasAuthority('PATIENT')")
    public ResponseEntity makePayment(@PathVariable("id") long appointmentId) {
        try {
            paymentService.savePaymentSuccess(appointmentId);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (ServiceException e) {
            ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    @PostMapping("/report/{id}")
    @PreAuthorize("hasAuthority('LAB_OPERATOR')")
    public @ResponseBody ResponseEntity uploadReport(@PathVariable("id") long appointmentTestId, @RequestBody UploadReportRequest request) {
        try {
            reportService.uploadReport(appointmentTestId, request);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (ServiceException e) {
            ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    private List<AllAppointmentResponse> mapAppointmentResponse(List<Appointment> appointments) {
        List<AllAppointmentResponse> list = new ArrayList<>();
        if (!appointments.isEmpty()) {
            appointments.forEach(appointment -> {
                try {
                    Payment payment = paymentService.findByAppointmentId(appointment.getId());
                    list.add(AllAppointmentResponse.builder().appointmentDate(appointment.getAppointmentDate())
                            .appointmentNumber(appointment.getAppointmentNumber())
                            .id(appointment.getId())
                            .amount(payment.getAmount())
                            .appointmentTime(appointment.getAppointmentTime())
                            .appointmentDate(appointment.getAppointmentDate())
                            .serviceCharge(payment.getServiceCharge())
                            .recommendedDoctor(appointment.getRecommendedDoctor())
                            .totalPay(payment.getTotalPay())
                            .status(appointment.getStatus().name())
                            .createDate(appointment.getCreatedAt())
                            .build());
                } catch (ServiceException e) {
                    log.error("Payment info {} error msg {} error {}", appointment.getAppointmentNumber(), e.getMessage(), e);
                    throw new RuntimeException(e);
                }
            });
        }
        return list;
    }

    private List<AllAppointmentForLabResponse> mapAppointmentResponseForALl(List<Appointment> appointments) {
        List<AllAppointmentForLabResponse> list = new ArrayList<>();
        if (!appointments.isEmpty()) {
            appointments.forEach(appointment -> {
                try {
                    Payment payment = paymentService.findByAppointmentId(appointment.getId());
                    list.add(AllAppointmentForLabResponse.builder().appointmentDate(appointment.getAppointmentDate())
                            .appointmentNumber(appointment.getAppointmentNumber())
                            .id(appointment.getId())
                            .amount(payment.getAmount())
                            .appointmentTime(appointment.getAppointmentTime())
                            .appointmentDate(appointment.getAppointmentDate())
                            .serviceCharge(payment.getServiceCharge())
                            .recommendedDoctor(appointment.getRecommendedDoctor())
                            .totalPay(payment.getTotalPay())
                            .status(appointment.getStatus().name())
                            .createDate(appointment.getCreatedAt())
                            .user(appointment.getUser().getName())
                            .userIdentity(appointment.getUser().getIdentityNo())
                            .build());
                } catch (ServiceException e) {
                    log.error("Payment info {} error msg {} error {}", appointment.getAppointmentNumber(), e.getMessage(), e);
                    throw new RuntimeException(e);
                }
            });
        }
        return list;
    }

    private String getLoggedUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null) {
            return "pub";
        }

        String user = null;
        if (auth.getPrincipal() instanceof UserDetails) {
            UserDetails details = (UserDetails) auth.getPrincipal();
            user = details.getUsername();
        } else {
            user = String.valueOf(auth.getPrincipal());
        }
        return user;
    }
}
