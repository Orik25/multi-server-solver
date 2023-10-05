package com.orik.clientserver.DAO;


import com.orik.clientserver.entities.Request;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RequestRepository extends JpaRepository<Request, Long> {
}