package com.orik.clientserver.service.interfaces;

import com.orik.clientserver.entities.Role;

public interface RoleService {
    Role findByName(String roleName);
    Role findById(Long id);
}