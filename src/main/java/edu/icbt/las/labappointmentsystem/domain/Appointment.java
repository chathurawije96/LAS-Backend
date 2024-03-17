package edu.icbt.las.labappointmentsystem.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;


import java.util.Date;
import java.util.List;

@Entity
@Table(name = "appointments")
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class Appointment extends EntityBase{
    @NotBlank
    private String appointmentNumber;
    @Column(nullable = false, updatable = false)
    @Temporal(TemporalType.DATE)
    private Date appointmentDate;
    @Column(nullable = false, updatable = false)
    @Temporal(TemporalType.TIME)
    private Date appointmentTime;
    private String recommendedDoctor;
    @OneToMany(mappedBy = "appointment")
    private List<AppointmentTests> appointmentTests;

    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;

    @Builder
    public Appointment(Long id, Status status, Date createdAt, Date updatedAt, String appointmentNumber, Date appointmentDate, Date appointmentTime, String recommendedDoctor, User user) {
        super(id, status, createdAt, updatedAt);
        this.appointmentNumber = appointmentNumber;
        this.appointmentDate = appointmentDate;
        this.appointmentTime = appointmentTime;
        this.recommendedDoctor = recommendedDoctor;
        this.user = user;
    }
}
