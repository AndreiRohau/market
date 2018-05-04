package by.ras;

import by.ras.entity.particular.User;
import by.ras.exception.ServiceException;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService{

    User add(User user) throws ServiceException;

    User findById(long id) throws ServiceException;
    User findByLogin(String login) throws ServiceException;
    List<User> findAll() throws ServiceException;

    User update(User user) throws ServiceException;

    void delete(long id) throws ServiceException;
}
