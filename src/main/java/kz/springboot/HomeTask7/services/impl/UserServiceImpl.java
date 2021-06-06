package kz.springboot.HomeTask7.services.impl;

import kz.springboot.HomeTask7.entities.Roles;
import kz.springboot.HomeTask7.entities.Users;
import kz.springboot.HomeTask7.repositories.RoleRepository;
import kz.springboot.HomeTask7.repositories.UserRepository;
import kz.springboot.HomeTask7.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {

        Users myUser = userRepository.findByEmail(s);

        if (myUser != null) {

            User secUser = new User(myUser.getEmail(), myUser.getPassword(), myUser.getRoles());
            return secUser;

        }

        throw new UsernameNotFoundException("User Not Found");
    }


    @Override
    public Users getUsersByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public Users createUser(Users user) {

        Users checkUser = userRepository.findByEmail(user.getEmail());

        if (checkUser == null) {
            Roles role = roleRepository.findByRole("ROLE_USER");

            if (role != null) {
                ArrayList<Roles> roles = new ArrayList<>();
                roles.add(role);
                user.setRoles(roles);
                user.setPassword(passwordEncoder.encode(user.getPassword()));
                return userRepository.save(user);

            }


        }

        return null;
    }

    @Override
    public List<Users> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public List<Roles> getAllRoles() {
        return roleRepository.findAll();
    }

    @Override
    public Users updateProfile(String email, String full_name) {

        Users myUser = userRepository.findByEmail(email);

        if (myUser != null) {
            myUser.setFullName(full_name);
            return userRepository.save(myUser);
        }
        return null;
    }

    @Override
    public Users updatePassword(Long id, String old_password, String new_password) {

        Users myUser = userRepository.getOne(id);

        if (passwordEncoder.matches(old_password, myUser.getPassword())) {
            myUser.setPassword(passwordEncoder.encode(new_password));
            return userRepository.save(myUser);
        }

        return null;
    }

    @Override
    public Roles addRole(String role, String description) {

        Roles checkRole = roleRepository.findByRole(role);

        if (checkRole == null) {
            Roles newRole = new Roles();
            newRole.setRole(role);
            newRole.setDescription(description);
            return roleRepository.save(newRole);
        }


        return null;
    }

    @Override
    public Users getUsersById(Long id) {
        return userRepository.getOne(id);
    }

    @Override
    public Roles getRolesById(Long role_id) {
        return roleRepository.getOne(role_id);
    }

    @Override
    public Users saveUser(Users user) {
        return userRepository.save(user);
    }
}
