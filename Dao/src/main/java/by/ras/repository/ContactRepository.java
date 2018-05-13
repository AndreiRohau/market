package by.ras.repository;

import by.ras.entity.particular.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface ContactRepository extends JpaRepository<Contact, Long> {
}
