package edu.icbt.las.labappointmentsystem.service;

import edu.icbt.las.labappointmentsystem.domain.EntityBase;
import edu.icbt.las.labappointmentsystem.domain.User;
import edu.icbt.las.labappointmentsystem.dto.RegisterRequest;
import edu.icbt.las.labappointmentsystem.dto.RegistrationVerifyRequest;
import edu.icbt.las.labappointmentsystem.exception.ServiceException;


public interface UserService extends GenericService<User, Long> {
    User findUserByEmail(String email) throws ServiceException;
    User register(RegisterRequest request) throws ServiceException;

    void registerVerify(RegistrationVerifyRequest request) throws ServiceException;
}
