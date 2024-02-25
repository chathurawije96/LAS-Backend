package edu.icbt.las.labappointmentsystem.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "payments")
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class Payment extends EntityBase{
    @NotBlank
    private BigDecimal amount;
    @NotBlank
    private BigDecimal serviceCharge;
    @NotBlank
    private BigDecimal totalPay;

    @OneToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinColumn(name = "appointment_id")
    private Appointment appointment;

    @Builder
    public Payment(Long id, Status status, Date createdAt, Date updatedAt, BigDecimal amount, BigDecimal serviceCharge, BigDecimal totalPay, Appointment appointment) {
        super(id, status, createdAt, updatedAt);
        this.amount = amount;
        this.serviceCharge = serviceCharge;
        this.totalPay = totalPay;
        this.appointment = appointment;
    }
}
