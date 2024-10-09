package net.epsi.mediscan.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.epsi.mediscan.entities.User;
import net.epsi.mediscan.repository.UserRepository;
import net.epsi.mediscan.service.IUserService;

@Service
public class UserServiceIMPL implements IUserService{

    @Autowired
    private UserRepository userRepository;

    @Override
    public User save(User user) {
        return this.userRepository.save(user);
    }

    @Override
    public void update(User user) {
        this.userRepository.save(user);
    }

    @Override
    public void delete(User user) {
        this.userRepository.delete(user);
    }

    @Override
    public User getById(long id) {
        return this.userRepository.findById(id).orElse(null);
    }

    @Override
    public List<User> getAll() {
        return this.userRepository.findAll();
    }

}
