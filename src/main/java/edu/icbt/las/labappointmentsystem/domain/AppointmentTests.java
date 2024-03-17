package edu.icbt.las.labappointmentsystem.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "appointment_tests")
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentTests extends EntityBase {
    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinColumn(name = "appointment_id")
    private Appointment appointment;
    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinColumn(name = "test_id")
    private Test test;

    @Builder
    public AppointmentTests(Long id, Status status, Date createdAt, Date updatedAt, Appointment appointment, Test test) {
        super(id, status, createdAt, updatedAt);
        this.appointment = appointment;
        this.test = test;
    }
}
