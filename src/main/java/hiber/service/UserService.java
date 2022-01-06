package hiber.service;

import hiber.model.User;

import java.util.List;

public interface UserService {
    void add(User user);

    User getUser(String model, int series);

    void cleanTables();

    List<User> listUsers();
}
