package by.ras;

import by.ras.impl.ContactServiceImpl;
import by.ras.impl.OrderServiceImpl;
import by.ras.impl.ProductServiceImpl;
import by.ras.impl.UserServiceImpl;
import by.ras.repository.ContactRepository;
import by.ras.repository.OrderRepository;
import by.ras.repository.ProductRepository;
import by.ras.repository.UserRepository;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {UserService.class, ContactService.class, ProductService.class, OrderService.class,
        UserServiceImpl.class, ContactServiceImpl.class, ProductServiceImpl.class, OrderServiceImpl.class,
        UserRepository.class, ContactRepository.class, ProductRepository.class, OrderRepository.class})
//@SpringBootTest(classes = UserRepository.class)
@DataJpaTest
@Transactional
public class BaseTest {

}
