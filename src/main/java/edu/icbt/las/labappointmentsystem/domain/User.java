package edu.icbt.las.labappointmentsystem.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;


import java.util.Date;

@Entity
@Table(name = "users")
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
//@ToString(callSuper = true, includeFieldNames = false)
public class User extends EntityBase {

  public User(Long id) {
    super.setId(id);
  }

  public enum UserType {
    ADMIN, LAB_OPERATOR, PATIENT
  }

  public enum IdType {
    NIC, DRIVING_LICENSE, PASSPORT
  }

  @Enumerated
  private UserType userType;

  @NotBlank
  private String name;

  @NotBlank
  private String username;

  private String email;

  private String password;

  private String mobile;

  @Enumerated
  private IdType idType;

  private String identityNo;

  @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
  @JoinColumn(name = "email_verification_id")
  private EmailVerification emailVerification;

  @Temporal(TemporalType.TIMESTAMP)
  private Date lastLoggedOn;

  @Builder

  public User(Long id, Status status, Date createdAt, Date updatedAt, UserType userType, String name, String username, String email, String password, String mobile, IdType idType, String identityNo, EmailVerification emailVerification, Date lastLoggedOn) {
    super(id, status, createdAt, updatedAt);
    this.userType = userType;
    this.name = name;
    this.username = username;
    this.email = email;
    this.password = password;
    this.mobile = mobile;
    this.idType = idType;
    this.identityNo = identityNo;
    this.emailVerification = emailVerification;
    this.lastLoggedOn = lastLoggedOn;
  }
}
