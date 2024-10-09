package net.epsi.mediscan.controller;

import net.epsi.mediscan.entities.User;
import net.epsi.mediscan.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/users") // Base URL for the User API
public class UserController {

    @Autowired
    private IUserService userService;

    // Endpoint to create a new user
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User createdUser = this.userService.save(user);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED); // Return created user with 201 status
    }

    // Endpoint to get a user by ID
    @GetMapping("/{id}")
public ResponseEntity<User> getUserById(@PathVariable Long id) {
    User user = userService.getById(id);
    if (user != null) {
        // S'assurer que la liste ordonnances est initialisée
        if (user.getOrdonnances() == null) {
            user.setOrdonnances(new ArrayList<>()); // Initialiser en tableau vide
        }
        return new ResponseEntity<>(user, HttpStatus.OK);
    } else {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}


    // Endpoint to get all users
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAll();
        // Parcours des utilisateurs pour s'assurer que la liste des ordonnances est initialisée
        for (User user : users) {
            if (user.getOrdonnances() == null) {
                user.setOrdonnances(new ArrayList<>()); // Initialiser si null
            }
        }

        if (!users.isEmpty()) {
            return new ResponseEntity<>(users, HttpStatus.OK); // Retourne la liste des utilisateurs
        }

        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK); // Retourne un tableau vide avec 200 OK
    }


    // Endpoint to update an existing user
    @PatchMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User user) {
        User existingUser = userService.getById(id);
        if (existingUser == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // Return 404 if user is not found
        }
        
        // Update user fields
        existingUser.setPrenom(user.getPrenom());
        existingUser.setNom(user.getNom());
        existingUser.setBirthday(user.getBirthday());

        userService.update(existingUser); // Call service to update user
        return new ResponseEntity<>(existingUser, HttpStatus.OK); // Return updated user with 200 OK status
    }

    // Endpoint to delete a user
    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteUser(@PathVariable Long id) {
        User user = userService.getById(id);
        if (user == null) {
            return new ResponseEntity<>(false, HttpStatus.NOT_FOUND); // Return 404 if user is not found
        }

        userService.delete(user); // Call service to delete user
        return new ResponseEntity<>(true, HttpStatus.OK); // Return true with 200 OK status
    }
}
