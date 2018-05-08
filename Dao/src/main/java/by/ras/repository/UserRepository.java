package by.ras.repository;

import by.ras.entity.particular.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("select u from User u where u.login = :login")
    User findByLogin(@Param("login") String login);


//    @Modifying
//    @Query(value = "UPDATE users SET name=?2, surname=?3, login=?4, password=?5, sex=?6, occupation=?7 where id = ?1", nativeQuery = true)
//    int updateUser(Long id, String name, String surname, String login, String password, String sex, String occupation);
//
//    @Query("UPDATE users SET name = name, surname = surname, login = login, password = password, sex = sex, occupation = occupation where id = id")
//    int updateUser(@Param("id") Long id, @Param("name") String name, @Param("surname") String surname,
//                   @Param("login") String login, @Param("password") String password,
//                   @Param("sex") Sex sex, @Param("occupation") Occupation occupation);

//    @Modifying
//    @Query(value = "UPDATE users set name=?2 where id=?1", nativeQuery = true)
//    int updateUserName(Long id, String name);
//    @Modifying
//    @Query(value = "UPDATE users set surname=?2 where id=?1", nativeQuery = true)
//    int updateUserSurname(Long id, String surname);
//    @Modifying
//    @Query(value = "UPDATE users set login=?2 where id=?1", nativeQuery = true)
//    int updateUserLogin(Long id, String login);
//    @Modifying
//    @Query(value = "UPDATE users set password=?2 where id=?1", nativeQuery = true)
//    int updateUserPassword(Long id, String password);
//    @Modifying
//    @Query(value = "UPDATE users set sex=?2 where id=?1", nativeQuery = true)
//    int updateUserSex(Long id, String sex);
//    @Modifying
//    @Query(value = "UPDATE users set occupation=?2 where id=?1", nativeQuery = true)
//    int updateUserOccupation(Long id, String occupation);
//    @Modifying
//    @Query(value = "UPDATE users set role=?2 where id=?1", nativeQuery = true)
//    int updateUserRole(Long id, String role);
//    @Modifying
//    @Query(value = "UPDATE users set status=?2 where id=?1", nativeQuery = true)
//    int updateUserStatus(Long id, String status);


}