package edu.icbt.las.labappointmentsystem.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "labs")
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class Lab extends EntityBase{

    private String name;
    private String number;

    @OneToMany(mappedBy = "lab")
    private List<Test> tests;


}
