package com.orik.applicationserver.service.impl;

import com.orik.applicationserver.DAO.RoleRepository;
import com.orik.applicationserver.entities.Role;
import com.orik.applicationserver.service.interfaces.RoleService;
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
        return roleRepository.findByName(roleName) //todo: add custom exception
                .orElseThrow(NullPointerException::new);
    }

    @Override
    public Role findById(Long id) {
        return roleRepository.findById(id) //todo: add custom exception
                .orElseThrow(NullPointerException::new);
    }
}