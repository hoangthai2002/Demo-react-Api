package vn.demo.jobhunter.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import vn.demo.jobhunter.domain.User;
import vn.demo.jobhunter.domain.dto.Meta;
import vn.demo.jobhunter.domain.dto.ResCreateUserDTO;
import vn.demo.jobhunter.domain.dto.ResGetIdUserDTO;
import vn.demo.jobhunter.domain.dto.ResUpdateUserDTO;
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
        // remove sensitive data
        List<ResGetIdUserDTO> listUser = pageUser.getContent()
                .stream().map(item -> new ResGetIdUserDTO(
                        item.getId(),
                        item.getName(),
                        item.getEmail(),
                        item.getGender(),
                        item.getAddress(),
                        item.getAge(),
                        item.getCreatedAt(),
                        item.getUpdatedAt()))
                .collect(Collectors.toList());
        rs.setResult(listUser);
        return rs;
    }

    public User handelPostUser(User user) {
        return this.userRepository.save(user);
    }

    public void handelDeleteUser(Long id) {
        this.userRepository.deleteById(id);
    }

    public User fetchUserById(long id) {
        Optional<User> useOptional = this.userRepository.findById(id);
        if (useOptional.isPresent()) {
            return useOptional.get();

        }
        return null;
    }

    public User handelUpdateUser(User reUSer) {
        User currentUser = this.getUserById(reUSer.getId());
        if (currentUser != null) {
            currentUser.setName(reUSer.getName());
            currentUser.setAddress(reUSer.getAddress());
            currentUser.setAge(reUSer.getAge());
            currentUser.setGender(reUSer.getGender());
            currentUser = this.userRepository.save(currentUser);
        }
        // update
        return currentUser;
    }

    public ResUpdateUserDTO convertResUpdateUserDTO(User user) {
        ResUpdateUserDTO rs = new ResUpdateUserDTO();

        rs.setId(user.getId());
        rs.setName(user.getName());
        rs.setAddress(user.getAddress());
        rs.setAge(user.getAge());
        rs.setGender(user.getGender());
        rs.setUpdateAt(user.getUpdatedAt());

        return rs;

    }

    public User handleGetUserByUsername(String username) {
        return this.userRepository.findByEmail(username);
    }

    public boolean isEmailExist(String email) {
        return this.userRepository.existsByEmail(email);
    }

    public ResCreateUserDTO covertToResCreateUserDTO(User user) {

        ResCreateUserDTO rs = new ResCreateUserDTO();
        rs.setId(user.getId());
        rs.setEmail(user.getEmail());
        rs.setName(user.getName());
        rs.setGender(user.getGender());
        rs.setAge(user.getAge());
        rs.setAddress(user.getAddress());
        rs.setCreatedAt(user.getCreatedAt());

        return rs;
    }

    public ResGetIdUserDTO convertToResGetIdUserDTO(User user) {
        ResGetIdUserDTO rs = new ResGetIdUserDTO();
        rs.setId(user.getId());
        rs.setAddress(user.getAddress());
        rs.setEmail(user.getEmail());
        rs.setGender(user.getGender());
        rs.setName(user.getName());
        rs.setAge(user.getAge());
        rs.setCreateAt(user.getCreatedAt());
        rs.setUpdateAt(user.getUpdatedAt());
        return rs;
    }

    public void updateUserToken(String token, String email) {
        User currentUser = this.handleGetUserByUsername(email);
        if (currentUser != null) {
            currentUser.setRefreshToken(token);
            this.userRepository.save(currentUser);
        }

    }

}
