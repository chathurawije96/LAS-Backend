package edu.icbt.las.labappointmentsystem.service.impl;

import edu.icbt.las.labappointmentsystem.domain.EmailVerification;
import edu.icbt.las.labappointmentsystem.domain.EntityBase;
import edu.icbt.las.labappointmentsystem.domain.User;
import edu.icbt.las.labappointmentsystem.dto.RegisterRequest;
import edu.icbt.las.labappointmentsystem.dto.RegistrationVerifyRequest;
import edu.icbt.las.labappointmentsystem.email.EmailSender;
import edu.icbt.las.labappointmentsystem.exception.DataAccessException;
import edu.icbt.las.labappointmentsystem.exception.ServiceException;
import edu.icbt.las.labappointmentsystem.exception.ServiceExceptionType;
import edu.icbt.las.labappointmentsystem.repository.UserRepository;
import edu.icbt.las.labappointmentsystem.service.UserService;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
@Slf4j
public class UserServiceImpl extends GenericServiceImpl<User, Long> implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailSender emailSender;

    @PostConstruct
    void init(){
        init(userRepository);
    }

    @Override
    public User findUserByEmail(String email) throws ServiceException {
        try {
            return userRepository.findUserByEmailAndStatus(email, EntityBase.Status.ACTIVE);
        } catch (DataAccessException e) {
            throw translateException(e);
        }
    }

    @Override
    public User register(RegisterRequest request) throws ServiceException {
        String otp = "" + ((int) (Math.random() * 9000) + 1000);
        User user = User.builder()
                .userType(User.UserType.PATIENT)
                .identityNo(request.getIdValue())
                .idType(User.IdType.valueOf(request.getIdType()))
                .lastLoggedOn(new Date())
                .name(request.getName())
                .mobile(request.getMobile())
                .email(request.getEmail())
                .createdAt(new Date())
                .updatedAt(new Date())
                .status(EntityBase.Status.VERIFICATION_PENDING)
                .username(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .emailVerification(EmailVerification.builder()
                        .codeSentOn(new Date())
                        .status(EntityBase.Status.VERIFICATION_PENDING)
                        .createdAt(new Date())
                        .updatedAt(new Date())
                        .source("Email")
                        .tries((byte) 0)
                        .verificationCode(otp)
                        .build())
                .build();
        User save = this.save(user);


        log.debug("Registration Email {} OTP : {}", save.getEmail(), otp);
        emailSender.sendEmail(request.getEmail().trim(),"LAS Verification",getOtpTemplateMessage(otp));

        return save;
    }

    @Override
    public void registerVerify(RegistrationVerifyRequest request) throws ServiceException {
        Optional<User> optionalUser = this.findById(request.getUserId());
        if (optionalUser.isPresent()){
            User user = optionalUser.get();
            if (user.getEmailVerification().getVerificationCode().equals(request.getOtp())){
                user.setStatus(EntityBase.Status.ACTIVE);
                user.getEmailVerification().setStatus(EntityBase.Status.VERIFICATION_SUCCESS);
                user.getEmailVerification().setVerifiedOn(new Date());
                user.getEmailVerification().setTriedOn(new Date());
                this.save(user);
            } else {
                user.getEmailVerification().setTriedOn(new Date());
                this.save(user);
                throw new ServiceException(ServiceExceptionType.VALIDATION_FAILED,"Email Verification Failed.. "+ user.getEmail());
            }
        } else {
            throw new ServiceException(ServiceExceptionType.VALIDATION_FAILED,"Invalid User");
        }
    }

    private String getOtpTemplateMessage(String otp) {
        String message = "Dear Patient,\n";
        message += "Please enter the code in your registration form.\n\n";
        message += "Code: " + otp + "\n";
        message += "Thank you for registering with us.\n\n";
        message += "LAS Team.\n";
        message += "www.las.lk";
        return message;
    }
}
