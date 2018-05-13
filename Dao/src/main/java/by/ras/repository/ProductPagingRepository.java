package by.ras.repository;

import by.ras.entity.particular.Order;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface ProductPagingRepository extends PagingAndSortingRepository<Order, Long> {
}
