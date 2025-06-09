package vn.demo.jobhunter.controller;

import org.springframework.web.bind.annotation.RestController;

import com.turkraft.springfilter.boot.Filter;

import vn.demo.jobhunter.domain.User;
import vn.demo.jobhunter.domain.dto.ResultPaginationDTO;
import vn.demo.jobhunter.service.UserService;
import vn.demo.jobhunter.util.annotation.ApiMessage;
import vn.demo.jobhunter.util.error.IdInvalidException;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
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
    @ApiMessage("fetch all users")
    public ResponseEntity<ResultPaginationDTO> getUserFindAll(@Filter Specification<User> spec, Pageable pageable) {

        return ResponseEntity.status(HttpStatus.OK).body(this.userService.fetchAllUser(spec, pageable));

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
