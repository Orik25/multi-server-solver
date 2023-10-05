package com.orik.clientserver.DTO.user;

import com.orik.clientserver.constant.RoleData;
import com.orik.clientserver.entities.Role;
import com.orik.clientserver.entities.User;
import com.orik.clientserver.service.interfaces.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Component
public class UserConverterDTO {
    private RoleService roleService;
    private PasswordEncoder passwordEncoder;
    @Lazy
    private Map<Boolean, String> roleMap;

    @Autowired
    public UserConverterDTO(RoleService roleService,PasswordEncoder passwordEncoder) {
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
        this.roleMap = createMapForUsersRoles();
    }

    public User convertToEntity(UserRegistrationDTO user){
        User newUser = new User();
        newUser.setEmail(user.getEmail());
        newUser.setFirstName(user.getFirstName());
        newUser.setLastName(user.getLastName());
        newUser.setPassword(passwordEncoder.encode(user.getPassword()));
        newUser.setRole(getRoleByUserType(user));
        newUser.setRequests(new ArrayList<>());

        return newUser;
    }

    private Role getRoleByUserType(UserRegistrationDTO user){
        return roleService.findByName(roleMap.get(user.getIsVip()));

    }

    private Map<Boolean, String> createMapForUsersRoles(){
        Map<Boolean, String> roleMap = new HashMap<>();
        roleMap.put(true, RoleData.VIP.getDBRoleName());
        roleMap.put(false, RoleData.USER.getDBRoleName());

        return roleMap;
    }
}