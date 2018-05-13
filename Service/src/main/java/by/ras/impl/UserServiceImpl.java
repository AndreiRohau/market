package by.ras.impl;

import by.ras.entity.Occupation;
import by.ras.entity.Role;
import by.ras.entity.Sex;
import by.ras.entity.particular.Contact;
import by.ras.entity.particular.Product;
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
                user.setRole(Role.CLIENT.name());
                userRepository.saveAndFlush(user);
            }
            return user;
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
    public User findByLogin(String login) throws ServiceException {
        try {
            return userRepository.findByLogin(login);
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
    public User edit(User user) throws ServiceException {
        try{
            return userRepository.saveAndFlush(user);
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
        }catch (Exception e){
            throw new ServiceException(e);
        }
    }

    @Override
    public User addReserve(long id, Product product) throws ServiceException {
        try {
            User user = userRepository.findOne(id);
            user.addReserve(product);
            return userRepository.saveAndFlush(user);
        }catch (Exception e){
            throw new ServiceException(e);
        }
    }

    @Override
    public User resetPassword(User user) throws ServiceException {
        try {
            User dbUser = userRepository.findOne(user.getId());
            //reset to [q1Q}
            if(dbUser != null){
                dbUser.setPassword("q1Q");
                userRepository.saveAndFlush(dbUser);
            }
            return dbUser;
        }catch (Exception e){
            log.info("Errors while executing : userRepository.findAll(pageRequest).getContent()");
            throw new ServiceException(e);
        }
    }

    @Override
    public User changeRole(User user) throws ServiceException {
        try {
            User dbUser = userRepository.findOne(user.getId());
            if(!dbUser.getRole().equals(Role.ADMIN.name()) && dbUser != null){
                log.info(dbUser.getRole());
                if(dbUser.getRole().equals(Role.BANNED.name())){
                    dbUser.setRole(Role.CLIENT.name());
                }else if (dbUser.getRole().equals(Role.CLIENT.name())){
                    dbUser.setRole(Role.BANNED.name());
                }
                dbUser = userRepository.saveAndFlush(dbUser);
            }
            return dbUser;
        }catch (Exception e){
            log.info("Errors while executing : userRepository.findAll(pageRequest).getContent()");
            throw new ServiceException(e);
        }
    }

    @Override
    public long countRows() throws ServiceException {
        try{
            return userRepository.count();
        }catch (Exception e){
            log.info("Errors while executing : userRepository.countRows()");
            throw new ServiceException(e);
        }
    }
}
