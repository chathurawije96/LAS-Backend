package edu.icbt.las.labappointmentsystem.service.impl;

import edu.icbt.las.labappointmentsystem.domain.User;
import edu.icbt.las.labappointmentsystem.repository.UserRepository;
import edu.icbt.las.labappointmentsystem.service.UserService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends GenericServiceImpl<User, Long> implements UserService {
    @Autowired
    private UserRepository userRepository;

    @PostConstruct
    void init(){
        init(userRepository);
    }
}
