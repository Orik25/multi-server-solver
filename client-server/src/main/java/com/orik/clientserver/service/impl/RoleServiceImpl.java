package com.orik.clientserver.service.impl;

import com.orik.clientserver.DAO.RoleRepository;
import com.orik.clientserver.entities.Role;
import com.orik.clientserver.exception.NoRoleFoundException;
import com.orik.clientserver.service.interfaces.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Autowired
    public RoleServiceImpl(RoleRepository roleService) {
        this.roleRepository = roleService;
    }

    @Override
    public Role findByName(String roleName) {
        return roleRepository.findByName(roleName)
                .orElseThrow(()-> new NoRoleFoundException("No role found with this name: " + roleName));
    }

    @Override
    public Role findById(Long id) {
        return roleRepository.findById(id)
                .orElseThrow(()->new NoRoleFoundException("No role found with this id: " + id));
    }
}