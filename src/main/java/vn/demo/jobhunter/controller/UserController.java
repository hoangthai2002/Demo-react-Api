package vn.demo.jobhunter.controller;

import org.springframework.web.bind.annotation.RestController;

import com.turkraft.springfilter.boot.Filter;

import jakarta.validation.Valid;
import vn.demo.jobhunter.domain.User;
import vn.demo.jobhunter.domain.response.ResCreateUserDTO;
import vn.demo.jobhunter.domain.response.ResUserDTO;
import vn.demo.jobhunter.domain.response.ResUpdateUserDTO;
import vn.demo.jobhunter.domain.response.ResultPaginationDTO;
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
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/api/v1")
public class UserController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<ResUserDTO> getUserById(@PathVariable("id") long id) throws IdInvalidException {

        User user = this.userService.fetchUserById(id);

        if (user == null) {
            throw new IdInvalidException("User với Id" + " " + id + " " + "không tồn tại");
        }

        return ResponseEntity.status(HttpStatus.OK).body(this.userService.convertToResGetIdUserDTO(user));
    }

    @GetMapping("/users")
    @ApiMessage("fetch all users")
    public ResponseEntity<ResultPaginationDTO> getUserFindAll(@Filter Specification<User> spec, Pageable pageable) {

        return ResponseEntity.status(HttpStatus.OK).body(this.userService.fetchAllUser(spec, pageable));

    }

    @PostMapping("/users")
    @ApiMessage("Create a new User ")
    public ResponseEntity<ResCreateUserDTO> newCreateUser(@Valid @RequestBody User postManUser)
            throws IdInvalidException {
        // check email có trùng hay ko
        boolean isEmailExist = this.userService.isEmailExist(postManUser.getEmail());

        if (isEmailExist) {
            throw new IdInvalidException(
                    "Email" + postManUser.getEmail() + "  " + "đã tồn tại,vui lòng sử dụng email khác");
        }

        String hashPassword = this.passwordEncoder.encode(postManUser.getPassword());
        postManUser.setPassword(hashPassword);
        User newUser = this.userService.handelCreateUser(postManUser);

        return ResponseEntity.status(HttpStatus.CREATED).body(this.userService.convertToResCreateUserDTO(newUser));
    }

    @DeleteMapping("/users/{id}")
    @ApiMessage("Delete User Success")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") long id)
            throws IdInvalidException {
        User currentUser = this.userService.fetchUserById(id);
        if (currentUser == null) {
            throw new IdInvalidException("User với id " + id + "không tồn tai");
        }

        this.userService.handelDeleteUser(id);
        return ResponseEntity.ok().body(null);

    }

    @PutMapping("/users")
    @ApiMessage("Update User Success")
    public ResponseEntity<ResUpdateUserDTO> updateUser(@RequestBody User user)
            throws IdInvalidException {

        User updateUser = this.userService.handelUpdateUser(user);
        if (updateUser == null) {
            throw new IdInvalidException("User với id " + user.getId() + "không tồn tại");
        }
        return ResponseEntity.ok().body(this.userService.convertResUpdateUserDTO(updateUser));
    }

}
