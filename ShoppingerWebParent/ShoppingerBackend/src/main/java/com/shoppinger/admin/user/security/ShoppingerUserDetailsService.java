package com.shoppinger.admin.user.security;

import com.shoppinger.admin.user.user.UserRepository;
import com.shoppinger.common.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class ShoppingerUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.getUserByEmail(email);
        if(user != null) {
            return new ShoppingerUserDetails(user);
        }
        throw new UsernameNotFoundException("There is no user with email" + email);
    }
}
