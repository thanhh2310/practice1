package com.example.practice_security_build.Init;

import com.example.practice_security_build.Enum.RoleName;
import com.example.practice_security_build.Model.Role;
import com.example.practice_security_build.Model.User;
import com.example.practice_security_build.Repository.RoleRepository;
import com.example.practice_security_build.Repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataInit {
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostConstruct
    public void init(){
        for(RoleName roleName: RoleName.values()){
            if(roleRepository.findByRoleName(roleName.name()) == null){
                Role role = new Role();
                role.setRoleName(roleName.name());
                roleRepository.save(role);
            }
        }

        if(userRepository.findByUsername("admin") == null){
            User user = new User();
            user.setUsername("admin");
            user.setPassword(passwordEncoder.encode("admin"));
            Role adminRole = roleRepository.findByRoleName(RoleName.ROLE_ADMIN.name());
            user.setRoles(List.of(adminRole));

            userRepository.save(user);
        }
    }
}
