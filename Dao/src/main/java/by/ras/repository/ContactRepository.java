package by.ras.repository;

import by.ras.entity.particular.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface ContactRepository extends JpaRepository<Contact, Long> {


    Contact findById(long id);

//    @Modifying
//    @Query(value = "UPDATE contacts set country=?2 where id=?1", nativeQuery = true)
//    int updateCountry(Long id, String country);
//    @Modifying
//    @Query(value = "UPDATE contacts set city=?2 where id=?1", nativeQuery = true)
//    int updateCity(Long id, String city);
//    @Modifying
//    @Query(value = "UPDATE contacts set street=?2 where id=?1", nativeQuery = true)
//    int updateStreet(Long id, String street);
//    @Modifying
//    @Query(value = "UPDATE contacts set house_number=?2 where id=?1", nativeQuery = true)
//    int updateHouseNumber(Long id, String houseNumber);
//    @Modifying
//    @Query(value = "UPDATE contacts set phone_number=?2 where id=?1", nativeQuery = true)
//    int updatePhoneNumber(Long id, String phoneNumber);

}
