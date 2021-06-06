package kz.springboot.HomeTask7.services;

import kz.springboot.HomeTask7.entities.Roles;
import kz.springboot.HomeTask7.entities.Users;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {

    Users getUsersByEmail(String email);

    Users createUser(Users user);

    List<Users> getAllUsers();

    List<Roles> getAllRoles();

    Users updateProfile(String email, String full_name);

    Users updatePassword(Long id, String old_password, String new_password);

    Roles addRole(String role, String description);

    Users getUsersById(Long id);

    Roles getRolesById(Long role_id);

    Users saveUser(Users user);
}
