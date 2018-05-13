package by.ras.repository;

import by.ras.BaseTest;
import by.ras.entity.ProductType;
import by.ras.entity.particular.Product;
import lombok.extern.log4j.Log4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;


import java.util.LinkedList;
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
        List<Product> products = fillTableProducts();
        Product p = new Product("bbbb","bbbb","bbbb","bbbb","2222","bbbb");
        productRepository.saveAndFlush(p);
        p = new Product("aaaa","aaaa","aaaa","aaaa","1111","aaaa");
        productRepository.saveAndFlush(p);
        Product product = productRepository.findByCompanyAndProductNameAndModelAndType("aaaa","aaaa","aaaa", "aaaa");
        assertEquals(p, product);

    }

    @Test
    public void findAllPredicat() throws Exception {
        List<Product> productsExpected = fillTableProducts();
        Product filter = new Product();
        List<Product> products = productRepository.findAll(searchProducts(filter));
        //filter.setProductType("CELLPHONE");
        //PageRequest pageRequest = new PageRequest(1, 5);
        //List<Product> products = productRepository.findAll(searchProducts(filter), pageRequest).getContent();
        //long rows = productRepository.count(searchProducts(filter));
        assertEquals(productsExpected, products);

    }


    private List<Product> fillTableProducts() {
        String[] companies = {"Apple", "Samsung", "XiaoMi", "Huawei", "Microsoft", "Dell", "Asus"};
        String[] types = {ProductType.CELLPHONE.name(), ProductType.LAPTOP.name(), ProductType.TV.name(),
                ProductType.TABLET.name(), ProductType.MP3.name(), ProductType.DISPLAY.name(),};
        List<Product> products = new LinkedList<>();
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
                    products.add(product);
                    productRepository.saveAndFlush(product);
                }
            }
        }
        return products;
    }

}