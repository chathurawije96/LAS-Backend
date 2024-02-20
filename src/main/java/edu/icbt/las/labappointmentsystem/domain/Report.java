package edu.icbt.las.labappointmentsystem.domain;

import jakarta.persistence.*;
import lombok.*;

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

    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinColumn(name = "report_generated_user_id")
    private User reportGeneratedUser;
}
