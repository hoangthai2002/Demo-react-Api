package vn.demo.jobhunter.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import vn.demo.jobhunter.domain.Company;
import vn.demo.jobhunter.domain.User;
import vn.demo.jobhunter.domain.response.ResCreateUserDTO;
import vn.demo.jobhunter.domain.response.ResUpdateUserDTO;
import vn.demo.jobhunter.domain.response.ResUserDTO;
import vn.demo.jobhunter.domain.response.ResultPaginationDTO;
import vn.demo.jobhunter.repository.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final CompanyService companyService;

    public UserService(UserRepository userRepository, CompanyService companyService) {
        this.userRepository = userRepository;
        this.companyService = companyService;
    }

    public User fetchUserById(long id) {
        Optional<User> optionalUser = this.userRepository.findById(id);
        if (optionalUser.isPresent()) {
            return optionalUser.get();
        }
        return null;
    }

    public User handelCreateUser(User user) {
        // check company
        if (user.getCompany() != null) {
            Optional<Company> companyOptional = this.companyService.findById(user.getCompany().getId());
            user.setCompany(companyOptional.isPresent() ? companyOptional.get() : null);
        }

        return this.userRepository.save(user);
    }

    public ResultPaginationDTO fetchAllUser(Specification<User> spec, Pageable pageable) {
        // đổi pageable sang list
        Page<User> pageUser = this.userRepository.findAll(spec, pageable);

        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta meta = new ResultPaginationDTO.Meta();

        meta.setPage(pageable.getPageNumber() + 1);
        meta.setPageSize(pageable.getPageSize());

        meta.setPages(pageUser.getTotalPages());
        meta.setTotal(pageUser.getTotalElements());

        rs.setMeta(meta);
        // remove sensitive data
        List<ResUserDTO> listUser = pageUser.getContent()
                .stream().map(item -> new ResUserDTO(
                        item.getId(),
                        item.getName(),
                        item.getEmail(),
                        item.getGender(),
                        item.getAddress(),
                        item.getAge(),
                        item.getCreatedAt(),
                        item.getUpdatedAt(),
                        new ResUserDTO.CompanyUser(
                                item.getCompany() != null ? item.getCompany().getId() : 0,
                                item.getCompany() != null ? item.getCompany().getName() : null)))

                .collect(Collectors.toList());
        rs.setResult(listUser);
        return rs;
    }

    public void handelDeleteUser(Long id) {
        this.userRepository.deleteById(id);
    }

    public User handelUpdateUser(User reUSer) {
        User currentUser = this.fetchUserById(reUSer.getId());
        if (currentUser != null) {
            currentUser.setName(reUSer.getName());
            currentUser.setAddress(reUSer.getAddress());
            currentUser.setAge(reUSer.getAge());
            currentUser.setGender(reUSer.getGender());

            // check company
            if (reUSer.getCompany() != null) {
                Optional<Company> companyOptional = this.companyService.findById(reUSer.getCompany().getId());
                currentUser.setCompany(companyOptional.isPresent() ? companyOptional.get() : null);
            }

            // update
            currentUser = this.userRepository.save(currentUser);
        }

        return currentUser;
    }

    public ResUpdateUserDTO convertResUpdateUserDTO(User user) {
        ResUpdateUserDTO rs = new ResUpdateUserDTO();
        ResUpdateUserDTO.CompanyUser com = new ResUpdateUserDTO.CompanyUser();
        if (user.getCompany() != null) {
            com.setId(user.getCompany().getId());
            com.setName(user.getCompany().getName());
            rs.setCompany(com);
        }

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

    public ResCreateUserDTO convertToResCreateUserDTO(User user) {

        ResCreateUserDTO rs = new ResCreateUserDTO();
        ResCreateUserDTO.CompanyUser com = new ResCreateUserDTO.CompanyUser();
        rs.setId(user.getId());
        rs.setEmail(user.getEmail());
        rs.setName(user.getName());
        rs.setGender(user.getGender());
        rs.setAge(user.getAge());
        rs.setAddress(user.getAddress());
        rs.setCreatedAt(user.getCreatedAt());

        if (user.getCompany() != null) {
            com.setId(user.getCompany().getId());
            com.setName(user.getCompany().getName());
            rs.setCompany(com);
        }

        return rs;
    }

    public ResUserDTO convertToResGetIdUserDTO(User user) {
        ResUserDTO rs = new ResUserDTO();
        ResUserDTO.CompanyUser com = new ResUserDTO.CompanyUser();
        rs.setId(user.getId());
        rs.setAddress(user.getAddress());
        rs.setEmail(user.getEmail());
        rs.setGender(user.getGender());
        rs.setName(user.getName());
        rs.setAge(user.getAge());
        rs.setCreateAt(user.getCreatedAt());
        rs.setUpdateAt(user.getUpdatedAt());

        if (user.getCompany() != null) {
            com.setId(user.getCompany().getId());
            com.setName(user.getCompany().getName());
            rs.setCompany(com);
        }
        return rs;
    }

    public void updateUserToken(String token, String email) {
        User currentUser = this.handleGetUserByUsername(email);
        if (currentUser != null) {
            currentUser.setRefreshToken(token);
            this.userRepository.save(currentUser);
        }

    }

    public User getUserByRefreshTokenAndByEmail(String token, String email) {
        return this.userRepository.findByRefreshTokenAndEmail(token, email);

    }

}
