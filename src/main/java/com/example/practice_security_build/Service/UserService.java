package com.example.practice_security_build.Service;

import com.example.practice_security_build.Enum.RoleName;
import com.example.practice_security_build.Model.Role;
import com.example.practice_security_build.Model.User;
import com.example.practice_security_build.Repository.RoleRepository;
import com.example.practice_security_build.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }

        return user;
    }

    public User registerUser(String username, String password) throws Exception {
        if (userRepository.findByUsername(username) != null){
            throw new Exception("User da ton tai");
        }

        Role userRole = roleRepository.findByRoleName(RoleName.ROLE_USER.name());
        if (userRole == null) {
            throw new Exception("Role ROLE_USER chưa được khởi tạo trong database");
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setRoleName(RoleName.ROLE_USER.name());
        user.setRoles(List.of(userRole));
        userRepository.save(user);
        return user;
    }

    public List<User> getAllUser(){
        return userRepository.findAll();
    }
}
