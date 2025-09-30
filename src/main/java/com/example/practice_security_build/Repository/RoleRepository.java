package com.example.practice_security_build.Repository;

import com.example.practice_security_build.Model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role,String> {
    Role findByRoleName(String roleName);
}
