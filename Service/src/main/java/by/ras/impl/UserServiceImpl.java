package by.ras.impl;

import by.ras.entity.Occupation;
import by.ras.entity.Role;
import by.ras.entity.Sex;
import by.ras.entity.Status;
import by.ras.entity.particular.Contact;
import by.ras.entity.particular.User;
import by.ras.exception.ServiceException;
import by.ras.repository.ContactRepository;
import by.ras.repository.UserRepository;
import by.ras.UserService;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import javax.annotation.PostConstruct;
import java.sql.Date;

import java.util.List;

@Service
@Transactional
@Log4j
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private final ContactRepository contactRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, ContactRepository contactRepository) {
        this.userRepository = userRepository;
        this.contactRepository = contactRepository;
    }

    @PostConstruct
    public void init() throws Exception {
        Date date = new Date(System.currentTimeMillis());
        try {
            if(userRepository.findByLogin("admin") == null){
                User admin = User.builder()
                        .name("Admin")
                        .surname("Admin")
                        .login("admin")
                        .password("q1Q")
                        .sex(Sex.MALE.name())
                        .occupation(Occupation.EMPLOYED.name())
                        .role(Role.ADMIN.name())
                        .status(Status.ACTIVE.name())
                        .date(new Date(System.currentTimeMillis()))
                        .build();
                userRepository.saveAndFlush(admin);
            }
            if(userRepository.findByLogin("user") == null){
                User user = User.builder()
                        .name("User")
                        .surname("User")
                        .login("user")
                        .password("q1Q")
                        .sex(Sex.MALE.name())
                        .occupation(Occupation.UNEMPLOYED.name())
                        .role(Role.CLIENT.name())
                        .status(Status.ACTIVE.name())
                        .date(new Date(System.currentTimeMillis()))
                        .build();
                userRepository.saveAndFlush(user);
                user = null;
            }

            User user = userRepository.findByLogin("admin");
            Contact contact;
            if(user.getContact() == null) {
                contact = Contact.builder()
                        .user(user)
                        .country("Cyprus")
                        .city("Sunny")
                        .street("Money")
                        .houseNumber("100")
                        .phoneNumber("000000000000")
                        .build();
                contactRepository.saveAndFlush(contact);
            }
            user = userRepository.findByLogin("user");
            if(user.getContact() == null) {
                contact = Contact.builder()
                        .user(user)
                        .country("Belarus")
                        .city("Minsk")
                        .street("Marx")
                        .houseNumber("10")
                        .phoneNumber("375291234567")
                        .build();
                contactRepository.saveAndFlush(contact);
            }
        }catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public User add(User user) throws ServiceException {
        try {
            if((userRepository.findByLogin(user.getLogin()) == null) && (user.getId() == 0)) {
                user.setDate(new Date(System.currentTimeMillis()));
                user.setStatus(Status.ACTIVE.name());
                user.setRole(Role.CLIENT.name());
                userRepository.saveAndFlush(user);
//                contactRepository.saveAndFlush(Contact.builder()
//                        .user(user)
//                        .country("default")
//                        .city("default")
//                        .street("default")
//                        .houseNumber("00")
//                        .phoneNumber("000000000000")
//                        .build());
            }
            return user;
        }catch (Exception e){
            throw new ServiceException(e);
        }
    }

    @Override
    public User update(User user) throws ServiceException {
        try {
            User dbUser = userRepository.findOne(user.getId());

            if(dbUser != null){
                dbUser.setName(user.getName());
                dbUser.setSurname(user.getSurname());
                dbUser.setLogin(user.getLogin());
                dbUser.setPassword(user.getPassword());
                dbUser.setSex(user.getSex());
                dbUser.setOccupation(user.getOccupation());

                userRepository.saveAndFlush(dbUser);
            }
            return dbUser;
//                log.info("before updates");
//                userRepository.updateUserName(dbUser.getId(), dbUser.getName());
//                userRepository.flush();
//                log.info("name");
//                userRepository.updateUserSurname(dbUser.getId(), dbUser.getSurname());
//                userRepository.flush();
//                log.info("surname");
//                userRepository.updateUserLogin(dbUser.getId(), dbUser.getLogin());
//                userRepository.flush();
//                log.info("login");
//                userRepository.updateUserPassword(dbUser.getId(), dbUser.getPassword());
//                userRepository.flush();
//                log.info("password");
//                userRepository.updateUserSex(dbUser.getId(), dbUser.getSex());
//                userRepository.flush();
//                log.info("Sex");
//                userRepository.updateUserOccupation(dbUser.getId(), dbUser.getOccupation());
//                userRepository.flush();
//                log.info("occupation");
//                dbUser = userRepository.findOne(dbUser.getId());
        }catch (Exception e){
            throw new ServiceException(e);
        }
    }

    @Override
    public User updateByAdmin(User user) throws ServiceException {
        try {
            User dbUser = userRepository.findOne(user.getId());

            if(dbUser != null){
                dbUser.setName(user.getName());
                dbUser.setSurname(user.getSurname());
                dbUser.setSex(user.getSex());
                dbUser.setOccupation(user.getOccupation());

                userRepository.saveAndFlush(dbUser);
            }
            return dbUser;
//                log.info("before updates");
//                userRepository.updateUserName(dbUser.getId(), dbUser.getName());
//                userRepository.flush();
//                log.info("name");
//                userRepository.updateUserSurname(dbUser.getId(), dbUser.getSurname());
//                userRepository.flush();
//                log.info("surname");
//                userRepository.updateUserLogin(dbUser.getId(), dbUser.getLogin());
//                userRepository.flush();
//                log.info("login");
//                userRepository.updateUserPassword(dbUser.getId(), dbUser.getPassword());
//                userRepository.flush();
//                log.info("password");
//                userRepository.updateUserSex(dbUser.getId(), dbUser.getSex());
//                userRepository.flush();
//                log.info("Sex");
//                userRepository.updateUserOccupation(dbUser.getId(), dbUser.getOccupation());
//                userRepository.flush();
//                log.info("occupation");
//                dbUser = userRepository.findOne(dbUser.getId());
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
    public List<User> findAll() throws ServiceException {
        try {
            return userRepository.findAll();
        }catch (Exception e){
            throw new ServiceException(e);
        }
    }

    @Override
    public List<User> findAll(PageRequest pageRequest) throws ServiceException {
        try {
            return userRepository.findAll(pageRequest).getContent();
        }catch (Exception e){
            log.info("Errors while executing : userRepository.findAll(pageRequest).getContent()");
            throw new ServiceException(e);
        }
    }

    @Override
    public User resetPassword(User user) throws ServiceException {
        try {
            User dbUser = userRepository.findOne(user.getId());

            if(dbUser != null){
                dbUser.setPassword(user.getPassword());
                userRepository.saveAndFlush(dbUser);
            }
            return dbUser;
        }catch (Exception e){
            log.info("Errors while executing : userRepository.findAll(pageRequest).getContent()");
            throw new ServiceException(e);
        }
    }

    @Override
    public User changeStatus(User user) throws ServiceException {
        try {
            User dbUser = userRepository.findOne(user.getId());

            if(dbUser != null){
                log.info(dbUser.getStatus());
                if(dbUser.getStatus().equals(Status.BLOCKED.name())){
                    dbUser.setStatus(Status.ACTIVE.name());
                }else if (dbUser.getStatus().equals(Status.ACTIVE.name())){
                    dbUser.setStatus(Status.BLOCKED.name());
                }
                dbUser = userRepository.saveAndFlush(dbUser);
            }
            log.info(dbUser.getStatus());
            return dbUser;
        }catch (Exception e){
            log.info("Errors while executing : userRepository.findAll(pageRequest).getContent()");
            throw new ServiceException(e);
        }
    }

    @Override
    public long countRows() throws ServiceException {
        try{
            long maxRows = userRepository.count();
            return maxRows;
        }catch (Exception e){
            log.info("Errors while executing : userRepository.countRows()");
            throw new ServiceException(e);
        }
    }
}
