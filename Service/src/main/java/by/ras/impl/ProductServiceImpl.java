package by.ras.impl;

import by.ras.ProductService;
import by.ras.entity.particular.Product;
import by.ras.exception.ServiceException;
import by.ras.repository.ProductRepository;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
@Log4j
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Product add(Product product) throws ServiceException {
        try {

            if((productRepository.findByModel(product.getModel()) == null) && (product.getId() == 0)) {
                productRepository.saveAndFlush(product);
            }
            return product;
        }catch (Exception e){
            throw new ServiceException(e);
        }
    }


    @Override
    public Product update(Product product) throws ServiceException {
        try {
            Product dbProduct = productRepository.findOne(product.getId());

            if(dbProduct != null){
                dbProduct.setCompany(product.getCompany());
                dbProduct.setProductName(product.getProductName());
                dbProduct.setModel(product.getModel());
                dbProduct.setProductType(product.getProductType());
                dbProduct.setPrice(product.getPrice());
                dbProduct.setDescription(product.getDescription());
                productRepository.saveAndFlush(dbProduct);
            }
            return product;
        }catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public Product findByModel(String model) throws ServiceException {
        try {
            return productRepository.findByModel(model);
        }catch (Exception e){
            throw new ServiceException(e);
        }
    }

    @Override
    public Product findById(long id) throws ServiceException {
        try {
            return productRepository.findOne(id);
        }catch (Exception e){
            throw new ServiceException(e);
        }
    }

    @Override
    public List<Product> findAll() throws ServiceException {
        try {
            return productRepository.findAll();
        }catch (Exception e){
            throw new ServiceException(e);
        }
    }

    @Override
    public void delete(long id) throws ServiceException {
        try {
            productRepository.delete(id);
        }catch (Exception e){
            throw new ServiceException(e);
        }
    }
}
