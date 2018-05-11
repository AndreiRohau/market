package by.ras.repository;

import by.ras.entity.particular.Order;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ProductPagingRepository extends PagingAndSortingRepository<Order, Long> {
}
