package by.ras.impl;

import by.ras.OrderService;
import by.ras.entity.OrderStatus;
import by.ras.entity.particular.Order;
import by.ras.entity.particular.User;
import by.ras.exception.ServiceException;
import by.ras.repository.OrderRepository;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.sql.Date;
import java.util.List;

@Service
@Transactional
@Log4j
public class OrderServiceImpl implements OrderService {

    private final
    OrderRepository orderRepository;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public Order addOrder(Order order) throws ServiceException {
        try {
            order.setDate(new Date(System.currentTimeMillis()));
            order.setOrderStatus(OrderStatus.NEW.name());
            return orderRepository.saveAndFlush(order);
        }catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public Order findById(long id) throws ServiceException {
        try {
            return orderRepository.findOne(id);
        }catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public List<Order> getAll() throws ServiceException {
        try {
            return orderRepository.findAll();
        }catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public Order editOrder(Order order) throws ServiceException {
        try {
            return orderRepository.saveAndFlush(order);
        }catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public void delete(long id) throws ServiceException {
        try {
            orderRepository.delete(id);
        }catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public List<Order> findByUserId(User user, PageRequest pageRequest) throws ServiceException {
        return null;
    }

    @Override
    public long countRows() throws ServiceException {
        try{
            long maxRows = orderRepository.count();
            return maxRows;
        }catch (Exception e){
            log.info("Errors while executing : orderRepository.countRows()");
            throw new ServiceException(e);
        }
    }
}
