package com.orik.applicationserver.service.interfaces;

import com.orik.applicationserver.entities.Role;

public interface RoleService {
    Role findByName(String roleName);
    Role findById(Long id);
}