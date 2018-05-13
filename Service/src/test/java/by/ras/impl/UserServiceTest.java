package by.ras.impl;

import by.ras.BaseTest;
import by.ras.ProductService;
import by.ras.UserService;
import by.ras.entity.Occupation;
import by.ras.entity.Role;
import by.ras.entity.Sex;
import by.ras.entity.particular.Contact;
import by.ras.entity.particular.Product;
import by.ras.entity.particular.User;
import by.ras.repository.ContactRepository;
import by.ras.repository.UserRepository;
import lombok.extern.log4j.Log4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;

@Log4j
@Transactional
public class UserServiceTest extends BaseTest {

    @Autowired
    UserRepository userRepository;
    @Autowired
    UserService userService;
    @Autowired
    ContactRepository contactRepository;
    @Autowired
    ProductService productService;

    @Test
    public void add() throws Exception {
        long[] init = initUsers();
        User user = User.builder()
                .name("Asd")
                .surname("Asd")
                .login("asd")
                .password("q1Q")
                .sex(Sex.MALE.name())
                .occupation(Occupation.EMPLOYED.name())
                .build();
        User newUser = userService.add(user);
        User foundUser = userRepository.findByLogin("asd");
        assertEquals(newUser, foundUser);
    }

    @Test
    public void findById() throws Exception {
        long[] init = initUsers();
        User foundUser = userService.findById(init[0]);
        User user = userRepository.findOne(init[0]);
        assertEquals(user, foundUser);
    }

    @Test
    public void findByLogin() throws Exception {
        long[] init = initUsers();
        User foundUser = userService.findByLogin(userRepository.findOne(init[0]).getLogin());
        User user = userRepository.findOne(init[0]);
        assertEquals(user, foundUser);
    }

    @Test
    public void findAll() throws Exception {
        long[] init = initUsers();
        int PAGE = 0;
        int OBJ_PER_PAGE = 3;
        PageRequest pageRequest = new PageRequest(PAGE, OBJ_PER_PAGE);

        List<User> foundUsers = userService.findAll(pageRequest);
        List<User> users = new LinkedList<>();
        for(int i = 0; i < OBJ_PER_PAGE && i < init.length; i++) {
            users.add(userRepository.findOne(init[i]));
        }
        assertEquals(users, foundUsers);
    }

    @Test
    public void edit() throws Exception {
        long[] init = initUsers();
        User toEdit = userRepository.findOne(init[0]);
        toEdit.setName("Asd");
        toEdit.setSurname("Asd");
        toEdit.setLogin("asd");
        User foundUser = userService.edit(toEdit);
        User user = userRepository.findOne(init[0]);
        assertEquals(user, foundUser);
    }

    @Test
    public void update() throws Exception {
        long[] init = initUsers();
        User toChange = userRepository.findOne(1L);
        toChange.setName("Aaa");
        toChange.setSurname("Aaa");
        toChange.setLogin("aaa");
        toChange.setPassword("a1Qa");
        toChange.setSex(Sex.FEMALE.name());
        toChange.setOccupation(Occupation.HOUSEWIFE.name());
        User user = userService.update(toChange);
        User foundUser = userRepository.findOne(1L);
        assertEquals(user, foundUser);
    }

    @Test
    public void updateByAdmin() throws Exception {
        long[] init = initUsers();
        User toChange = userRepository.findOne(1L);
        toChange.setName("Aaa");
        toChange.setSurname("Aaa");
        toChange.setSex(Sex.FEMALE.name());
        toChange.setOccupation(Occupation.HOUSEWIFE.name());
        User user = userService.updateByAdmin(toChange);
        User foundUser = userRepository.findOne(1L);
        assertEquals(user, foundUser);
    }

    @Test
    public void addReserve() throws Exception {
        Product product = productService.findById(1L);
        long[] init = initUsers();
        userService.addReserve(init[0], product);
        Product foundProduct = userRepository.findOne(init[0]).getReservedProducts().get(0);
        assertEquals(product, foundProduct);
    }

    @Test
    public void resetPassword() throws Exception {
        long[] init = initUsers();
        User admin = userRepository.findOne(init[0]);
        admin.setPassword("w2W");
        admin = userRepository.saveAndFlush(admin);
        User tmp = new User();
        tmp.setId(init[0]);
        User foundUser = userService.resetPassword(tmp);
        assertEquals(admin, foundUser);
    }

    @Test
    public void changeRole() throws Exception {
        long[] init = initUsers();
        User user = userRepository.findOne(init[1]);
        String role = user.getRole();
        user = userService.changeRole(user);
        String foundRole = userService.changeRole(user).getRole();
        assertEquals(role, foundRole);

        User admin = userRepository.findOne(init[0]);
        role = admin.getRole();
        foundRole = userService.changeRole(admin).getRole();
        assertEquals(role, foundRole);
    }

    @Test
    public void countRows() throws Exception {
        long[] init = initUsers();
        long foundRows = userService.countRows();
        assertEquals(init.length, foundRows);
    }

    public long[] initUsers() throws Exception {
        long[] result = new long[2];
        Date date = new Date(System.currentTimeMillis());
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
            admin = userRepository.saveAndFlush(admin);
        } else {
            result[0] = userRepository.findByLogin("admin").getId();
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
            user = userRepository.saveAndFlush(user);
        } else {
            result[1] = userRepository.findByLogin("user").getId();
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
        return result;
    }
}