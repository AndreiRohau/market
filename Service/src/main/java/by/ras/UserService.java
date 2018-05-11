package by.ras;

import by.ras.entity.particular.Product;
import by.ras.entity.particular.User;
import by.ras.exception.ServiceException;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface UserService {

    User add(User user) throws ServiceException;

    User findById(long id) throws ServiceException;
    User findByLogin(String login) throws ServiceException;
    List<User> findAll() throws ServiceException;
    List<User> findAll(PageRequest pageRequest) throws ServiceException;

    User update(User user) throws ServiceException;

    User addReserve(long id, Product product) throws ServiceException;

    User updateByAdmin(User user) throws ServiceException;

    void delete(long id) throws ServiceException;
    User resetPassword(User user) throws ServiceException;

    User changeStatus(User user) throws ServiceException;

    long countRows() throws ServiceException;
}
