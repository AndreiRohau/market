package by.ras.impl;

import by.ras.ProductService;
import by.ras.entity.ProductType;
import by.ras.entity.particular.Product;
import by.ras.entity.particular.User;
import by.ras.exception.ServiceException;
import by.ras.repository.ProductRepository;
import by.ras.repository.UserRepository;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;

import java.util.List;

import static by.ras.repository.specification.ProductSpecification.searchProducts;

@Service
@Transactional
@Log4j
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, UserRepository userRepository) {
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    @PostConstruct
    public void init() throws Exception {
        String[] companies = {"Apple", "Samsung", "XiaoMi", "Huawei", "Microsoft", "Dell", "Asus"};
        String[] types = {ProductType.CELLPHONE.name(), ProductType.LAPTOP.name(), ProductType.TV.name(),
                ProductType.TABLET.name(), ProductType.MP3.name(), ProductType.DISPLAY.name(),};
        try {
            for(int i = 0; i < companies.length; i++) {
                for(int m = 0; m < types.length; m++) {
                    if (productRepository.findByCompanyAndProductNameAndModelAndType(companies[i], "any_name",
                            companies[i].toLowerCase().concat("-" + m), types[m]) == null) {
                        Product product = Product.builder()
                                .company(companies[i])
                                .productName("any_name")
                                .model(companies[i].toLowerCase().concat("-" + m))
                                .productType(types[m])
                                .price(String.valueOf((i + 1) * (m + 1)))
                                .description("any_text")
                                .build();
                        productRepository.saveAndFlush(product);
                    }
                }
            }

        }catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public Product add(Product product) throws ServiceException {
        try {
            if((productRepository.findByCompanyAndProductNameAndModelAndType(product.getCompany(),
                    product.getProductName(), product.getModel(), product.getProductType()) == null)
                    && (product.getId() == 0)) {
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
    public Product findById(long id) throws ServiceException {
        try {
            return productRepository.findOne(id);
        }catch (Exception e){
            throw new ServiceException(e);
        }
    }

    @Override
    public List<Product> findAll(PageRequest pageRequest) throws ServiceException {
        try {
            return productRepository.findAll(pageRequest).getContent();
        }catch (Exception e){
            log.info("Errors while executing : productRepository.findAll(pageRequest).getContent()");
            throw new ServiceException(e);
        }
    }

    @Override
    public List<Product> findAllComplex(Product p, PageRequest pageRequest) throws ServiceException {
        try {
            return productRepository.findAll(searchProducts(p), pageRequest).getContent();
        }catch (Exception e){
            log.info("Errors while executing : productRepository.findAll(pageRequest).getContent()");
            throw new ServiceException(e);
        }
    }

    @Override
    public void delete(long id) throws ServiceException {
        try {
            Product product = productRepository.findOne(id);
            List<User> users = product.getPersonsHaveReserved();
            for (User user : users) {
                user.cancelReserve(product);
                userRepository.saveAndFlush(user);
            }
            productRepository.delete(id);
        }catch (Exception e){
            throw new ServiceException(e);
        }
    }

    @Override
    public long countRows() throws ServiceException {
        try{
            return productRepository.count();
        }catch (Exception e){
            log.info("Errors while executing : productRepository.count()");
            throw new ServiceException(e);
        }
    }

    @Override
    public long countRowsComplex(Product p) throws ServiceException {
        try{
            return productRepository.count(searchProducts(p));
        }catch (Exception e){
            log.info("Errors while executing : productRepository.countCOMPLEX()");
            throw new ServiceException(e);
        }
    }

}
