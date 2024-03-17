package edu.icbt.las.labappointmentsystem.dto;

import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MakeAppointmentResponse {
    private long appointmentId;
    private String appointmentNumber;
    @NonNull
    private BigDecimal amount;
    @NonNull
    private BigDecimal serviceCharge;
    @NonNull
    private BigDecimal totalPay;
}
