package com.orik.clientserver.service.interfaces;

import com.orik.clientserver.entities.Request;
import org.springframework.data.domain.Page;

public interface RequestService {

    Page<Request> getAllRequestsSorted(int page, int size, String sortField, String sortOrder);

    void deleteById(Long id);

}