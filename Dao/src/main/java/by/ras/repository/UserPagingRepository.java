package by.ras.repository;

import by.ras.entity.particular.User;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface UserPagingRepository extends PagingAndSortingRepository<User, Long> {
}
