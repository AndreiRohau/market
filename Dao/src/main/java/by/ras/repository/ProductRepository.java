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

}
