package by.ras;

import by.ras.entity.particular.User;

import java.util.List;

public interface UserService {

    User add(User user);

    User findById(long id);
    User fingByLogin(String login);
    List<User> findAll();

    User update(User user);

    void delete(long id);
}
