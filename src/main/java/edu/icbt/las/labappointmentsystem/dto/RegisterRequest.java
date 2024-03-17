package edu.icbt.las.labappointmentsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
    private String idType;
    private String idValue;
    private String name;
    private String email;
    private String mobile;
    private String password;
}
