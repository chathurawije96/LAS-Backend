package edu.icbt.las.labappointmentsystem.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "reports")
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class Report extends EntityBase {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appointment_test_id")
    private AppointmentTests appointmentTests;

    @Lob
    private String report;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "report_generated_user_id")
    private User reportGeneratedUser;

    @Builder
    public Report(Long id, Status status, Date createdAt, Date updatedAt, AppointmentTests appointmentTests, String report, User reportGeneratedUser) {
        super(id, status, createdAt, updatedAt);
        this.appointmentTests = appointmentTests;
        this.report = report;
        this.reportGeneratedUser = reportGeneratedUser;
    }
}
