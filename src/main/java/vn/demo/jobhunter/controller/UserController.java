package vn.demo.jobhunter.controller;

import org.springframework.web.bind.annotation.RestController;

import vn.demo.jobhunter.domain.User;
import vn.demo.jobhunter.service.UserService;
import vn.demo.jobhunter.util.error.IdInvalidException;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
public class UserController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUser(@PathVariable("id") long id) {
        User user = this.userService.getUserById(id);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getUserFindAll() {
        // List<User> listUser=this.userService.fetchAllUser();
        return ResponseEntity.status(HttpStatus.OK).body(this.userService.fetchAllUser());

    }

    @PostMapping("/users")
    public ResponseEntity<User> newCreateUser(@RequestBody User postManUser) {
        String hashPassword = this.passwordEncoder.encode(postManUser.getPassword());
        postManUser.setPassword(hashPassword);
        User newUser = this.userService.handelPostUser(postManUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable("id") long id) throws IdInvalidException {

        if (id >= 1500) {
            throw new IdInvalidException("Id khong lon hon 1500");
        }
        this.userService.handelDeleteUser(id);
        return ResponseEntity.ok("Delete Success");
        // return ResponseEntity.status(HttpStatus.OK).body("Delete Success");
    }

    @PutMapping("/users")
    public ResponseEntity<User> updateUser(@RequestBody User user) {
        User updateUser = this.userService.handelUpdateUser(user);
        return ResponseEntity.ok().body(updateUser);
    }

}
