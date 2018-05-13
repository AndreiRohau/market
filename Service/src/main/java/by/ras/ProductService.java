package by.ras;

import by.ras.entity.particular.Product;
import by.ras.exception.ServiceException;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface ProductService {

    Product add(Product product) throws ServiceException;

    Product findIfSuchExists(Product product) throws ServiceException;
    boolean isUnique(Product product) throws ServiceException;
    Product findById(long id) throws ServiceException;
    List<Product> findAll() throws ServiceException;
    List<Product> findAll(PageRequest pageRequest) throws ServiceException;
    List<Product> findAllComplex(Product p, PageRequest pageRequest) throws ServiceException;

    Product update(Product product) throws ServiceException;
//    Product updateByAdmin(Product product) throws ServiceException;

    void delete(long id) throws ServiceException;

    long countRows() throws ServiceException;
    long countRowsComplex(Product product) throws ServiceException;

}
