package by.ras;

import by.ras.entity.particular.Order;
import by.ras.exception.ServiceException;

import java.util.List;

public interface OrderService {
    Order addOrder(Order order) throws ServiceException;
    Order findById(long id) throws ServiceException;
    List<Order> getAll() throws ServiceException ;
    Order editOrder(Order order) throws ServiceException ;
    void delete(long id) throws ServiceException ;
}
