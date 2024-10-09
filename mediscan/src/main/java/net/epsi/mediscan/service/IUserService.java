package net.epsi.mediscan.service;

import java.util.List;

import net.epsi.mediscan.entities.User;

public interface IUserService {
    User save(User user);
    void update (User user);
    void delete(User user);
    User getById(long id);
    List<User> getAll();

}
