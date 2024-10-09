package net.epsi.mediscan.controller;

import net.epsi.mediscan.entities.Medicament;
import net.epsi.mediscan.entities.Ordonnance;
import net.epsi.mediscan.entities.User;
import net.epsi.mediscan.service.IUserService;
import net.epsi.mediscan.utils.DateUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private IUserService userService;

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User tempUser = this.userService.save(user);

        for (Ordonnance ordonnance : user.getOrdonnances()) {
            ordonnance.setUser(tempUser);
            for (Medicament medicament : ordonnance.getMedicaments()) {
                medicament.setOrdonnance(ordonnance);
            }
        }

        if (!DateUtils.checkDateMedicamentInOrdonnance(user.getOrdonnances())) {
            return ResponseEntity.badRequest().build();
        }

        User createdUser = this.userService.save(tempUser);

        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        User user = userService.getById(id);
        if (user != null) {

            if (user.getOrdonnances() == null) {
                user.setOrdonnances(new ArrayList<>());
            }
            return new ResponseEntity<>(user, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAll();
        for (User user : users) {
            if (user.getOrdonnances() == null) {
                user.setOrdonnances(new ArrayList<>());
            }
        }

        if (!users.isEmpty()) {
            return new ResponseEntity<>(users, HttpStatus.OK);
        }

        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User user) {
        User existingUser = userService.getById(id);
        if (existingUser == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        if (!DateUtils.checkDateMedicamentInOrdonnance(user.getOrdonnances())) {
            return ResponseEntity.badRequest().build();
        }

        existingUser.setPrenom(user.getPrenom());
        existingUser.setNom(user.getNom());
        existingUser.setBirthday(user.getBirthday());

        userService.update(existingUser);
        return new ResponseEntity<>(existingUser, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteUser(@PathVariable Long id) {
        User user = userService.getById(id);
        if (user == null) {
            return new ResponseEntity<>(false, HttpStatus.NOT_FOUND);
        }

        userService.delete(user);
        return new ResponseEntity<>(true, HttpStatus.OK);
    }
}
