package edu.icbt.las.labappointmentsystem.domain;

import lombok.*;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.util.Date;

@Entity
@Table(name = "email_verifications")
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
//@ToString(callSuper = true, includeFieldNames = false)
public class EmailVerification extends EntityBase {

  @NotBlank
  private String source;

  private String verificationCode;

  private byte tries;

  @Temporal(TemporalType.TIMESTAMP)
  private Date codeSentOn;

  @Temporal(TemporalType.TIMESTAMP)
  private Date codeExpiresOn;

  @Temporal(TemporalType.TIMESTAMP)
  private Date verifiedOn;

  @Temporal(TemporalType.TIMESTAMP)
  private Date triedOn;

  @Builder

  public EmailVerification(Long id, Status status, Date createdAt, Date updatedAt, String source, String verificationCode, byte tries, Date codeSentOn, Date codeExpiresOn, Date verifiedOn, Date triedOn) {
    super(id, status, createdAt, updatedAt);
    this.source = source;
    this.verificationCode = verificationCode;
    this.tries = tries;
    this.codeSentOn = codeSentOn;
    this.codeExpiresOn = codeExpiresOn;
    this.verifiedOn = verifiedOn;
    this.triedOn = triedOn;
  }
}
