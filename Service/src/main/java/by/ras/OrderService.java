package by.ras;

import by.ras.entity.particular.Order;
import by.ras.entity.particular.User;
import by.ras.exception.ServiceException;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface OrderService {
    Order addOrder(Order order) throws ServiceException;

    Order findById(long id) throws ServiceException;
    List<Order> findAll(PageRequest pageRequest) throws ServiceException;
    List<Order> findAllComplex(Order filter, PageRequest pageRequest) throws ServiceException;

    Order editOrder(Order order) throws ServiceException;

    long countRows() throws ServiceException;
    long countRowsComplex(Order order) throws ServiceException;
}
