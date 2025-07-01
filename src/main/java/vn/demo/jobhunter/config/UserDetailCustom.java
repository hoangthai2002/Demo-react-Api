package vn.demo.jobhunter.config;

import java.util.Collections;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import vn.demo.jobhunter.service.UserService;

@Component("userDetailsService")
public class UserDetailCustom implements UserDetailsService {

    private UserService userService;

    public UserDetailCustom(UserService userService) {
        this.userService = userService;

    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        vn.demo.jobhunter.domain.User user = this.userService.handleGetUserByUsername(username);
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));

    }

}
