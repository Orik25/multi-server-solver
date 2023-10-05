package com.orik.applicationserver.repository;


import com.orik.applicationserver.entities.Request;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RequestRepository extends JpaRepository<Request, Long> {
}