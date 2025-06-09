package vn.demo.jobhunter.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import vn.demo.jobhunter.domain.User;
import vn.demo.jobhunter.domain.dto.Meta;
import vn.demo.jobhunter.domain.dto.ResultPaginationDTO;
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

    public ResultPaginationDTO fetchAllUser(Specification<User> spec, Pageable pageable) {
        // đổi pageable sang list
        Page<User> pageUser = this.userRepository.findAll(spec, pageable);

        ResultPaginationDTO rs = new ResultPaginationDTO();
        Meta meta = new Meta();

        meta.setPage(pageable.getPageNumber() + 1);
        meta.setPageSize(pageable.getPageSize());

        meta.setPages(pageUser.getTotalPages());
        meta.setTotal(pageUser.getTotalElements());

        rs.setMeta(meta);

        rs.setResult(pageUser.getContent());

        return rs;
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
