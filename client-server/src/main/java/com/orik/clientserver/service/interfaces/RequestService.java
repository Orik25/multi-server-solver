package com.orik.clientserver.service.interfaces;

import com.orik.clientserver.entities.Request;
import org.springframework.data.domain.Page;

public interface RequestService {

    Request addNew(Request request);

    Page<Request> getAllRequestsSorted(int page, int size, String sortField, String sortOrder,Long userId);

    void deleteById(Long id);

}