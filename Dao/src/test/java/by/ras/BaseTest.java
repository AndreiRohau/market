package by.ras;

import by.ras.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = UserRepository.class)
@Transactional
//@SpringBootTest(classes = UserRepository.class)
@DataJpaTest
public class BaseTest {

}
