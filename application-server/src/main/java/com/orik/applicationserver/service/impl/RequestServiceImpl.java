package com.orik.applicationserver.service.impl;

import com.orik.applicationserver.DAO.RequestRepository;
import com.orik.applicationserver.entities.Request;
import com.orik.applicationserver.exception.NoRequestFoundException;
import com.orik.applicationserver.service.interfaces.RequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class RequestServiceImpl implements RequestService {

    private final RequestRepository requestRepository;

    @Autowired
    public RequestServiceImpl(RequestRepository requestRepository) {
        this.requestRepository = requestRepository;
    }

    @Override
    public Page<Request> getAllRequestsSorted(int page, int size, String sortField, String sortOrder) {
        Sort sort = Sort.by(sortField);

        if ("desc".equals(sortOrder)) {
            sort = sort.descending();
        }

        PageRequest pageRequest = PageRequest.of(page, size, sort);
        return requestRepository.findAll(pageRequest);
    }

    @Override
    public void deleteById(Long id) {
        Request request = requestRepository.findById(id)
                .orElseThrow(()-> new NoRequestFoundException("Impossible to update the Request. Request not found with id: " + id));
    }
}