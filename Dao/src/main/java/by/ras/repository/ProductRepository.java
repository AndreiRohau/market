package by.ras.repository;

import by.ras.entity.particular.Product;
import by.ras.entity.particular.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("select p from Product p where p.model = :model")
    Product findByModel(@Param("model") String model);

}
