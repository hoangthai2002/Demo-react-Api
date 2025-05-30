package vn.demo.jobhunter.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import vn.demo.jobhunter.domain.User;
import vn.demo.jobhunter.repository.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getUserById(long id) {
        Optional<User> optionalUser = this.userRepository.findById(id);
        if (optionalUser.isPresent()) {
            return optionalUser.get();
        }
        return null;
    }

    public List<User> fetchAllUser() {
        return this.userRepository.findAll();
    }

    public User handelPostUser(User user) {
        return this.userRepository.save(user);
    }

    public void handelDeleteUser(Long id) {
        this.userRepository.deleteById(id);
    }

    public User handelUpdateUser(User reUSer) {
        User currentUser = this.getUserById(reUSer.getId());
        if (currentUser != null) {
            currentUser.setName(reUSer.getName());
            currentUser.setEmail(reUSer.getEmail());
            currentUser.setPassword(reUSer.getPassword());
            currentUser = this.userRepository.save(currentUser);
        }

        return currentUser;
    }

    public User handleGetUserByUsername(String username) {
        return this.userRepository.findByEmail(username);
    }
}
