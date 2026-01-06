package com.goSocial.goSocial.service;

import com.goSocial.goSocial.Repository.UserRepository;
import com.goSocial.goSocial.model.User;
import com.goSocial.goSocial.model.UserPrinciple;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUserName(username);
        System.out.println("Inside UserService " + user.getPassword() + " " + username );
        if(user == null){
            System.out.println("User Not found");
            throw new UsernameNotFoundException("user not found");
        }

        return new UserPrinciple(user);
    }


}
