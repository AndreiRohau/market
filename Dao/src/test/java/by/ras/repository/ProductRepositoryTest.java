package by.ras.repository;

import by.ras.BaseTest;
import by.ras.entity.ProductType;
import by.ras.entity.particular.Product;
import lombok.extern.log4j.Log4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

import static by.ras.repository.specification.ProductSpecification.searchProducts;
import static org.junit.Assert.*;

@Log4j
@Transactional
public class ProductRepositoryTest extends BaseTest {

    @Autowired
    ProductRepository productRepository;

    @Test
    public void findByCompanyAndProductNameAndModelAndType() throws Exception {

    }

    private void fillTableProducts() {
        String[] companies = {"Apple", "Samsung", "XiaoMi", "Huawei", "Microsoft", "Dell", "Asus"};
        String[] types = {ProductType.CELLPHONE.name(), ProductType.LAPTOP.name(), ProductType.TV.name(),
                ProductType.TABLET.name(), ProductType.MP3.name(), ProductType.DISPLAY.name(),};
        for (int i = 0; i < companies.length; i++) {
            for (int m = 0; m < types.length; m++) {
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
    }

    @Test
    public void findAllComplex() throws Exception {
        fillTableProducts();

        Product p = new Product();
        p.setProductType("CELLPHONE");

//        int rows = productRepository.countComplex(p.getCompany(), p.getProductName(), p.getModel(), p.getProductType(), p.getPrice());

//        List<Product> products = productRepository.findAll(p.getCompany(), p.getProductName(),
//                p.getModel(), p.getProductType(), p.getPrice());
//        List<Product> products = productRepository
//                .findAll(p.getCompany(), p.getProductName(), p.getModel(), p.getProductType(), p.getPrice());

        log.info("***********************************");
        log.info("***********************************");
        log.info("***********************************");
//        log.info("rows " + rows);
//        log.info("products size = " + products.size());
//        products.forEach(product -> log.info(product));
        log.info("***********************************");
        log.info("***********************************");
        log.info("***********************************");
    }

    @Test
    public void findAllPredicat() throws Exception {
        fillTableProducts();

        Product filter = new Product();
        filter.setProductType("CELLPHONE");
        PageRequest pageRequest = new PageRequest(1, 5);

        long rows = productRepository.count(searchProducts(filter));

        List<Product> products = productRepository.findAll(searchProducts(filter), pageRequest).getContent();

        log.info("***********************************");
        log.info("***********************************");
        log.info("***********************************");
        log.info("rows " + rows);
        log.info("products size = " + products.size());
        products.forEach(product -> log.info(product));
        log.info("***********************************");
        log.info("***********************************");
        log.info("***********************************");

    }
}