package by.ras.impl;

import by.ras.entity.Occupation;
import by.ras.entity.Role;
import by.ras.entity.Sex;
import by.ras.entity.Status;
import by.ras.entity.particular.User;
import by.ras.exception.ServiceException;
import by.ras.repository.UserRepository;
import by.ras.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.time.LocalDateTime;

import java.util.Collections;
import java.util.List;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostConstruct
    public void init() throws Exception {
        if(userRepository.findByLogin("admin") == null){
            User admin = User.builder()
                    .name("Admin")
                    .surname("Admin")
                    .login("admin")
                    .password("a1Z")
                    .sex(Sex.MALE)
                    .occupation(Occupation.EMPLOYED)
                    .role(Role.ADMIN)
                    .status(Status.ACTIVE)
                    .date(LocalDateTime.now())
                    .build();
            userRepository.saveAndFlush(admin);
        }
        if(userRepository.findByLogin("user") == null){
            User user = User.builder()
                    .name("User")
                    .surname("User")
                    .login("user")
                    .password("a1Z")
                    .sex(Sex.MALE)
                    .occupation(Occupation.UNEMPLOYED)
                    .role(Role.CLIENT)
                    .status(Status.ACTIVE)
                    .date(LocalDateTime.now())
                    .build();
            userRepository.saveAndFlush(user);
        }
    }



    @Override
    public User add(User user) throws ServiceException {
        try {
            return userRepository.saveAndFlush(user);
        }catch (Exception e){
            throw new ServiceException(e);
        }
    }

    @Override
    public void delete(long id) throws ServiceException {
        try {
            userRepository.delete(id);
        }catch (Exception e){
            throw new ServiceException(e);
        }
    }

    @Override
    public User findByLogin(String login) throws ServiceException {
        try {
            return userRepository.findByLogin(login);
        }catch (Exception e){
            throw new ServiceException(e);
        }
    }

    @Override
    public User findById(long id) throws ServiceException {
        try {
            return userRepository.findOne(id);
        }catch (Exception e){
            throw new ServiceException(e);
        }
    }

    @Override
    public User update(User user) throws ServiceException {
        try {
            return userRepository.saveAndFlush(user);
        }catch (Exception e){
            throw new ServiceException(e);
        }
    }

    @Override
    public List<User> findAll() throws ServiceException {
        try {
            return userRepository.findAll();
        }catch (Exception e){
            throw new ServiceException(e);
        }
    }
}
