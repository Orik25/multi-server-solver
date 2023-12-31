package com.orik.clientserver.DAO;


import com.orik.clientserver.entities.Request;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RequestRepository extends JpaRepository<Request, Long> {

    Page<Request> findByUserId(Long userId, PageRequest pageRequest);
}