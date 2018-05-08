package by.ras;

import by.ras.entity.particular.Order;
import by.ras.entity.particular.User;
import by.ras.exception.ServiceException;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface OrderService {
    Order addOrder(Order order) throws ServiceException;
    Order findById(long id) throws ServiceException;
    List<Order> getAll() throws ServiceException;
    Order editOrder(Order order) throws ServiceException;
    void delete(long id) throws ServiceException;

    List<Order> findByUserId(User user, PageRequest pageRequest) throws ServiceException;

    long countRows() throws ServiceException;
}
