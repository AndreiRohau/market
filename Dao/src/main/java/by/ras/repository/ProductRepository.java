package by.ras.repository;

import by.ras.entity.particular.Product;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository

public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product>{

    @Query("select p from Product p where p.company = :company and p.productName = :productName and p.model = :model " +
            "and p.productType = :productType")
    Product findByCompanyAndProductNameAndModelAndType(@Param("company") String company,
                                                       @Param("productName") String productName,
                                                       @Param("model") String model,
                                                       @Param("productType") String productType);













//    @Query(value = "SELECT * FROM products WHERE (company = ?1 OR ?1 IS NULL) " +
//            "AND (product_name = ?2 OR ?2 IS NULL) AND (model = ?3 OR ?3 IS NULL) " +
//            "AND (product_type = ?4 OR ?4 IS NULL) AND (price = ?5 OR ?5 IS NULL)",
//            nativeQuery = true)
//    List<Product> findAll(String company,
//                          String productName,
//                          String model,
//                          String productType,
//                          String price);
//
//    @Query(value = "SELECT count(*) FROM products WHERE (company = ?1 OR ?1 IS NULL) " +
//            "AND (product_name = ?2 OR ?2 IS NULL) AND (model = ?3 OR ?3 IS NULL) " +
//            "AND (product_type = ?4 OR ?4 IS NULL) AND (price = ?5 OR ?5 IS NULL)",
//            nativeQuery = true)
//    int countComplex(String company,
//                          String productName,
//                          String model,
//                          String productType,
//                          String price);
//
//    @Query(value = "SELECT * FROM products WHERE (company = ?1 OR ?1 IS NULL) " +
//            "AND (product_name = ?2 OR ?2 IS NULL) AND (model = ?3 OR ?3 IS NULL) " +
//            "AND (product_type = ?4 OR ?4 IS NULL) AND (price = ?5 OR ?5 IS NULL)",
//            countQuery = "SELECT count(*) FROM products WHERE (company = ?1 OR ?1 IS NULL) " +
//                    "AND (product_name = ?2 OR ?2 IS NULL) AND (model = ?3 OR ?3 IS NULL) " +
//                    "AND (product_type = ?4 OR ?4 IS NULL) AND (price = ?5 OR ?5 IS NULL)",
//            nativeQuery = true)
//    List<Product> findAllComplex(String mycompany,
//                                 String myproductName,
//                                 String mymodel,
//                                 String myproductType,
//                                 String myprice,
//                                 Pageable pageable);
//
//    List<Product> findAll(@Param("company") String company,
//                          @Param("productName") String productName,
//                          @Param("model") String model,
//                          @Param("productType") String productType,
//                          @Param("price") String price);
}
