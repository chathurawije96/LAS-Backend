package edu.icbt.las.labappointmentsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AllAppointmentResponse {
    private long id;
    private String appointmentNumber;
    private Date appointmentDate;
    private Date appointmentTime;
    private String recommendedDoctor;
    private BigDecimal amount;
    private BigDecimal serviceCharge;
    private BigDecimal totalPay;
    private String status;
    private Date createDate;
}
